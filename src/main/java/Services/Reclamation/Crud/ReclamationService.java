package Services.Reclamation.Crud;

import Models.Reclamation.Reclamation;
import Models.Reclamation.Statut;
import Models.Reclamation.TypeReclamation;
import Services.Reclamation.Interface.Ireclamation;
import Utils.Mydatasource;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReclamationService implements Ireclamation<Reclamation> {

    private static final String BASE_URL = "http://192.168.1.169:8080/suivi?reclam=";
    private final Connection connection;

    public ReclamationService() {
        this.connection = Mydatasource.getInstance().getConnection();
    }

    @Override
    public void AjouterRec(Reclamation reclamation) {
        if (reclamation.getStatut() == null) {
            reclamation.setStatut(Statut.EN_ATTENTE);
        }

        // Ajout du champ date dans la requête SQL
        String req = "INSERT INTO reclamation (idUser, titre, description, type, statut, date, qr_code_url) VALUES (?, ?, ?, ?, ?, ?, ?)";
        Connection conn = null;
        try {
            conn = Mydatasource.getInstance().getConnection();
            conn.setAutoCommit(false);
            try (PreparedStatement ps = conn.prepareStatement(req, Statement.RETURN_GENERATED_KEYS)) {
                ps.setInt(1, reclamation.getIdUser());
                ps.setString(2, reclamation.getTitre());
                ps.setString(3, reclamation.getDescription());
                ps.setString(4, reclamation.getType().getLabel());
                ps.setString(5, reclamation.getStatut().getLabel());
                ps.setObject(6, reclamation.getDate()); // Ajout de la date
                ps.setString(7, "");
                ps.executeUpdate();
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    int id = rs.getInt(1);
                    reclamation.setId(id);
                    String reclamationInfo = String.format(
                            "Réclamation ID: %d\nTitre: %s\nDescription: %s\nType: %s\nStatut: %s\nDate: %s",
                            id,
                            reclamation.getTitre(),
                            reclamation.getDescription(),
                            reclamation.getType().getLabel(),
                            reclamation.getStatut().getLabel(),
                            reclamation.getDate().toString() // Ajout de la date dans le QR code
                    );
                    String qrFilePath = generateQRCode(reclamationInfo, id);
                    String updateReq = "UPDATE reclamation SET qr_code_url = ? WHERE id = ?";
                    try (PreparedStatement updatePs = conn.prepareStatement(updateReq)) {
                        updatePs.setString(1, ""); // Laisser vide car nous n'utilisons plus une URL
                        updatePs.setInt(2, id);
                        updatePs.executeUpdate();
                    }
                    reclamation.setQrCodeUrl(""); // Pas d'URL, juste un texte
                    System.out.println("Réclamation ajoutée avec succès ! QR Code généré : " + qrFilePath);
                }
                conn.commit();
            }
        } catch (SQLException | WriterException | IOException e) {
            System.out.println("Erreur lors de l'ajout de la réclamation : " + e.getMessage());
            e.printStackTrace();
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            throw new RuntimeException("Erreur lors de l'ajout de la réclamation", e);
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    private String generateQRCode(String url, int id) throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(url, BarcodeFormat.QR_CODE, 200, 200);
        String filePath = "qr_reclamation_" + id + ".png";
        MatrixToImageWriter.writeToPath(bitMatrix, "PNG", FileSystems.getDefault().getPath(filePath));
        return filePath;
    }

    @Override
    public List<Reclamation> RechercherRec() {
        String req = "SELECT Id, idUser, Titre, Description, Type, Statut, date, qr_code_url FROM reclamation"; // Ajout de date
        List<Reclamation> reclamations = new ArrayList<>();

        try (Connection conn = Mydatasource.getInstance().getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(req)) {

            while (rs.next()) {
                Reclamation r = new Reclamation();
                r.setId(rs.getInt("Id"));
                r.setIdUser(rs.getInt("idUser"));
                r.setTitre(rs.getString("Titre"));
                r.setDescription(rs.getString("Description"));
                r.setType(TypeReclamation.fromLabel(rs.getString("Type")));
                r.setStatut(Statut.fromLabel(rs.getString("Statut")));
                r.setDate(rs.getObject("date", LocalDateTime.class)); // Récupération de la date
                r.setQrCodeUrl(rs.getString("qr_code_url"));
                reclamations.add(r);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des réclamations : " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de la récupération des réclamations", e);
        }
        return reclamations;
    }

    public Reclamation findById(int id) {
        long startTime = System.currentTimeMillis();
        System.out.println("Recherche de la réclamation avec ID : " + id);
        String req = "SELECT Id, idUser, Titre, Description, Type, Statut, date, qr_code_url FROM reclamation WHERE Id = ?"; // Ajout de date
        try (Connection conn = Mydatasource.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(req)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Reclamation r = new Reclamation();
                    r.setId(rs.getInt("Id"));
                    r.setIdUser(rs.getInt("idUser"));
                    r.setTitre(rs.getString("Titre"));
                    r.setDescription(rs.getString("Description"));
                    r.setType(TypeReclamation.fromLabel(rs.getString("Type")));
                    r.setStatut(Statut.fromLabel(rs.getString("Statut")));
                    r.setDate(rs.getObject("date", LocalDateTime.class)); // Récupération de la date
                    r.setQrCodeUrl(rs.getString("qr_code_url"));
                    System.out.println("Réclamation trouvée : " + r.getTitre());
                    long endTime = System.currentTimeMillis();
                    System.out.println("Temps de traitement de findById : " + (endTime - startTime) + " ms");
                    return r;
                } else {
                    System.out.println("Aucune réclamation trouvée pour l'ID : " + id);
                }
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération de la réclamation : " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de la récupération de la réclamation", e);
        }
        long endTime = System.currentTimeMillis();
        System.out.println("Temps de traitement de findById : " + (endTime - startTime) + " ms");
        return null;
    }
    @Override
    public void ModifierRec(Reclamation reclamation) {
        String req = "UPDATE reclamation SET titre = ?, description = ?, type = ? WHERE id = ?";
        try (Connection conn = Mydatasource.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(req)) {
            ps.setString(1, reclamation.getTitre());
            ps.setString(2, reclamation.getDescription());
            ps.setString(3, reclamation.getType().getLabel());
            ps.setInt(4, reclamation.getId());
            int rowsUpdated = ps.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Réclamation mise à jour avec succès !");
                Reclamation updatedReclamation = findById(reclamation.getId());
                if (updatedReclamation != null) {
                    String reclamationInfo = generateReclamationText(updatedReclamation);
                    generateQRCode(reclamationInfo, reclamation.getId());
                } else {
                    System.out.println("Erreur : Impossible de récupérer la réclamation mise à jour pour régénérer le QR code.");
                }
            } else {
                System.out.println("Aucune réclamation trouvée avec cet ID !");
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la mise à jour de la réclamation : " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de la mise à jour de la réclamation", e);
        } catch (WriterException | IOException e) {
            System.out.println("Erreur lors de la régénération du QR code : " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void SupprimerRec(Reclamation reclamation) {
        String req = "DELETE FROM reclamation WHERE id = ?";
        try (Connection conn = Mydatasource.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(req)) {
            ps.setInt(1, reclamation.getId());
            int rowsDeleted = ps.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Réclamation supprimée avec succès !");
            } else {
                System.out.println("Aucune réclamation trouvée avec cet ID !");
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la suppression de la réclamation : " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de la suppression de la réclamation", e);
        }
    }

    @Override
    public List<Reclamation> RechercherRecParMotCle(String motCle) {
        String req = "SELECT Id, idUser, Titre, Description, Type, date, qr_code_url FROM reclamation WHERE Titre LIKE ? OR Description LIKE ? OR Type LIKE ?"; // Ajout de date
        List<Reclamation> reclamations = new ArrayList<>();
        try (Connection conn = Mydatasource.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(req)) {
            ps.setString(1, "%" + motCle + "%");
            ps.setString(2, "%" + motCle + "%");
            ps.setString(3, "%" + motCle + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Reclamation r = new Reclamation();
                    r.setId(rs.getInt("Id"));
                    r.setIdUser(rs.getInt("idUser"));
                    r.setTitre(rs.getString("Titre"));
                    r.setDescription(rs.getString("Description"));
                    r.setType(TypeReclamation.fromLabel(rs.getString("Type")));
                    r.setDate(rs.getObject("date", LocalDateTime.class)); // Récupération de la date
                    r.setQrCodeUrl(rs.getString("qr_code_url"));
                    reclamations.add(r);
                }
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des réclamations par mot-clé : " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de la récupération des réclamations par mot-clé", e);
        }
        return reclamations;
    }
    @Override
    public void ModifierStatut(Reclamation reclamation) {
        String req = "UPDATE reclamation SET statut = ? WHERE id = ?";
        try (Connection conn = Mydatasource.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(req)) {
            ps.setString(1, reclamation.getStatut().getLabel());
            ps.setInt(2, reclamation.getId());
            int rowsUpdated = ps.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Statut de la réclamation mis à jour avec succès !");

                Reclamation updatedReclamation = findById(reclamation.getId());
                if (updatedReclamation != null) {
                    String reclamationInfo = generateReclamationText(updatedReclamation);
                    generateQRCode(reclamationInfo, reclamation.getId());
                } else {
                    System.out.println("Erreur : Impossible de récupérer la réclamation mise à jour pour régénérer le QR code.");
                }
            } else {
                System.out.println("Aucune réclamation trouvée avec cet ID !");
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la mise à jour du statut de la réclamation : " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de la mise à jour du statut de la réclamation", e);
        } catch (WriterException | IOException e) {
            System.out.println("Erreur lors de la régénération du QR code : " + e.getMessage());
            e.printStackTrace();
        }
    }
    private String generateReclamationText(Reclamation reclamation) {
        return String.format(
                "Réclamation ID: %d\nTitre: %s\nDescription: %s\nType: %s\nStatut: %s\nDate: %s",
                reclamation.getId(),
                reclamation.getTitre(),
                reclamation.getDescription(),
                reclamation.getType().getLabel(),
                reclamation.getStatut().getLabel(),
                reclamation.getDate().toString() // Ajout de la date
        );
    }
    public List<Reclamation> RechercherRecParUtilisateur(int idUser) {
        String req = "SELECT Id, idUser, Titre, Description, Type, Statut, date, qr_code_url FROM reclamation WHERE idUser = ?"; // Ajout de date
        List<Reclamation> reclamations = new ArrayList<>();

        try (Connection conn = Mydatasource.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(req)) {
            ps.setInt(1, idUser);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Reclamation r = new Reclamation();
                    r.setId(rs.getInt("Id"));
                    r.setIdUser(rs.getInt("idUser"));
                    r.setTitre(rs.getString("Titre"));
                    r.setDescription(rs.getString("Description"));
                    r.setType(TypeReclamation.fromLabel(rs.getString("Type")));
                    r.setStatut(Statut.fromLabel(rs.getString("Statut")));
                    r.setDate(rs.getObject("date", LocalDateTime.class)); // Récupération de la date
                    r.setQrCodeUrl(rs.getString("qr_code_url"));
                    reclamations.add(r);
                }
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des réclamations de l'utilisateur : " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de la récupération des réclamations de l'utilisateur", e);
        }
        return reclamations;
    }
}