package Services.Utilisateur.Crud;

import Models.Utilisateur.Role;
import Models.Utilisateur.Utilisateurs;
import Services.Utilisateur.Interface.Imembres;
import Utils.Mydatasource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MembresService implements Imembres<Utilisateurs> {

    private static final String ROLE_MEMBRE = "MEMBRE";
    private final Connection connection = Mydatasource.getInstance().getConnection();
    private final BrevoEmailSender emailSender = new BrevoEmailSender();

    @Override
    public void AjouterMem(Utilisateurs utilisateurs) {
        String req = "INSERT INTO membres (Nom, Prénom, Email, CIN, Adresse, NumTel, Role, MotDePasse, Image, token, isConfirmed) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(req, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, utilisateurs.getNom());
            ps.setString(2, utilisateurs.getPrenom());
            ps.setString(3, utilisateurs.getEmail());
            ps.setString(4, utilisateurs.getCin());
            ps.setString(5, utilisateurs.getAdresse());
            ps.setString(6, utilisateurs.getNumTel());
            ps.setString(7, ROLE_MEMBRE);
            ps.setString(8, utilisateurs.getMotDePasse());
            ps.setString(9, utilisateurs.getImage());
            ps.setString(10, utilisateurs.getToken()); // Token généré dans le constructeur Utilisateurs
            ps.setBoolean(11, utilisateurs.isConfirmed()); // False par défaut

            int rowsInserted = ps.executeUpdate();
            if (rowsInserted > 0) {
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        utilisateurs.setId(generatedKeys.getInt(1));
                        System.out.println("Membre ajouté avec succès, ID : " + utilisateurs.getId());
                    }
                }

                // Envoyer un email d'activation
                try {
                    String activationLink = "http://localhost:8080/activate?token=" + utilisateurs.getToken();
                    String emailContent = "<h1>Bienvenue " + utilisateurs.getPrenom() + " !</h1>"
                            + "<p>Votre compte a été créé avec succès. Cliquez sur le lien suivant pour activer votre compte :</p>"
                            + "<a href=\"" + activationLink + "\">Activer mon compte</a>";
                    emailSender.sendEmail(utilisateurs.getEmail(), "Activez votre compte", emailContent);
                    System.out.println("Email d'activation envoyé avec succès à " + utilisateurs.getEmail());
                } catch (Exception emailException) {
                    System.err.println("Erreur lors de l'envoi de l'email : " + emailException.getMessage());
                }
            } else {
                throw new SQLException("Échec de l'insertion dans la base de données.");
            }
        } catch (SQLException e) {
            System.err.println("Erreur SQL lors de l'ajout du membre : " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Erreur SQL lors de l'ajout du membre", e);
        }
    }

    // Nouvelle méthode pour activer un compte via le token
    public boolean activateAccount(String token) {
        String req = "UPDATE membres SET isConfirmed = TRUE, token = NULL WHERE token = ? AND isConfirmed = FALSE";
        try (PreparedStatement ps = connection.prepareStatement(req)) {
            ps.setString(1, token);
            int rowsUpdated = ps.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Compte activé avec succès pour le token : " + token);
                return true;
            } else {
                System.out.println("Aucun compte trouvé ou déjà activé pour le token : " + token);
                return false;
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'activation du compte : " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void ModifierMem(Utilisateurs membre) {
        String req = "UPDATE membres SET Nom = ?, Prenom = ?, Email = ?, CIN = ?, Adresse = ?, NumTel = ?, Role = ?, MotDePasse = ?, Image = ?, isConfirmed = ?, token = ? WHERE Id = ?";

        try (PreparedStatement ps = connection.prepareStatement(req)) {
            ps.setString(1, membre.getNom());
            ps.setString(2, membre.getPrenom());
            ps.setString(3, membre.getEmail());
            ps.setString(4, membre.getCin());
            ps.setString(5, membre.getAdresse());
            ps.setString(6, membre.getNumTel());
            ps.setString(7, membre.getRole().toString());
            ps.setString(8, membre.getMotDePasse());
            ps.setString(9, membre.getImage());
            ps.setBoolean(10, membre.isConfirmed());
            ps.setString(11, membre.getToken());
            ps.setInt(12, membre.getId());

            int rowsUpdated = ps.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Membre mis à jour avec succès !");
            } else {
                System.out.println("Aucun membre trouvé avec cet ID !");
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la mise à jour du membre : " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void SupprimerMem(Utilisateurs membre) {
        String req = "DELETE FROM membres WHERE Id = ?";

        try (PreparedStatement ps = connection.prepareStatement(req)) {
            ps.setInt(1, membre.getId());

            int rowsDeleted = ps.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Membre supprimé avec succès !");
            } else {
                System.out.println("Aucun membre trouvé avec cet ID !");
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression du membre : " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public List<Utilisateurs> RechercherMem() {
        String req = "SELECT * FROM membres";
        List<Utilisateurs> membres = new ArrayList<>();

        try (Statement st = connection.createStatement();
             ResultSet rs = st.executeQuery(req)) {

            while (rs.next()) {
                Utilisateurs m = extraireMembre(rs);
                membres.add(m);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des membres : " + e.getMessage());
            e.printStackTrace();
        }

        return membres;
    }

    public Utilisateurs rechercherMem(int id) {
        String req = "SELECT * FROM membres WHERE Id = ?";
        try (PreparedStatement ps = connection.prepareStatement(req)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return extraireMembre(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche du membre par ID : " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public Utilisateurs rechercherMemParNom(String email) {
        String req = "SELECT * FROM membres WHERE Email = ?";
        try (PreparedStatement ps = connection.prepareStatement(req)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return extraireMembre(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche par nom : " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
    public Utilisateurs rechercherMemParEmail(String email) {
        String req = "SELECT * FROM membres WHERE Email = ?";
        try (PreparedStatement ps = connection.prepareStatement(req)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Utilisateurs utilisateur = new Utilisateurs();
                    utilisateur.setId(rs.getInt("Id"));
                    utilisateur.setNom(rs.getString("Nom"));
                    utilisateur.setPrenom(rs.getString("Prénom"));
                    utilisateur.setEmail(rs.getString("Email"));
                    utilisateur.setCin(rs.getString("CIN"));
                    utilisateur.setAdresse(rs.getString("Adresse"));
                    utilisateur.setNumTel(rs.getString("NumTel"));
                    utilisateur.setRole(Role.valueOf(rs.getString("Role"))); // Récupérer le rôle
                    utilisateur.setMotDePasse(rs.getString("MotDePasse"));
                    utilisateur.setImage(rs.getString("Image"));
                    utilisateur.setToken(rs.getString("token"));
                    utilisateur.setConfirmed(rs.getBoolean("isConfirmed"));
                    return utilisateur;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int getIdByNomPrenom(String nomPrenom) {
        int id = -1;
        String[] parts = nomPrenom.split(" ");
        if (parts.length == 2) {
            String nom = parts[0];
            String prenom = parts[1];
            String query = "SELECT Id FROM membres WHERE Nom = ? AND Prénom = ?";
            try (PreparedStatement ps = connection.prepareStatement(query)) {
                ps.setString(1, nom);
                ps.setString(2, prenom);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        id = rs.getInt("Id");
                    }
                }
            } catch (SQLException e) {
                System.err.println("Erreur lors de la récupération de l'ID : " + e.getMessage());
                e.printStackTrace();
            }
        }
        return id;
    }

    public List<String> getAllUserEmails() {
        List<String> emails = new ArrayList<>();
        String query = "SELECT Email FROM membres";

        try (PreparedStatement ps = connection.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                String email = rs.getString("Email");
                if (email != null && !email.trim().isEmpty()) {
                    emails.add(email);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des emails : " + e.getMessage());
            e.printStackTrace();
        }
        return emails;
    }

    private Utilisateurs extraireMembre(ResultSet rs) throws SQLException {
        return new Utilisateurs(
                rs.getInt("Id"),
                rs.getString("Nom"),
                rs.getString("Prénom"),
                rs.getString("CIN"),
                rs.getString("Email"),
                rs.getString("Adresse"),
                rs.getString("NumTel"),
                parseRole(rs.getString("Role")),
                rs.getString("MotDePasse"),
                rs.getString("Image"),
                rs.getString("token"),
                rs.getBoolean("isConfirmed")
        );
    }

    private Role parseRole(String roleStr) {
        if (roleStr == null || roleStr.trim().isEmpty()) {
            return Role.MEMBRE;
        }
        try {
            return Role.valueOf(roleStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            System.err.println("Rôle inconnu : " + roleStr + " - Utilisation de MEMBRE par défaut.");
            return Role.MEMBRE;
        }
    }
    public String getUserNameById(int userId) {
        String req = "SELECT nom FROM users WHERE id = ?"; // Ajustez selon votre structure (par exemple, "username" au lieu de "nom")
        try (PreparedStatement ps = connection.prepareStatement(req)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("nom");
            } else {
                return "Utilisateur inconnu"; // Valeur par défaut si l'utilisateur n'est pas trouvé
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération du nom de l'utilisateur : " + e.getMessage());
            e.printStackTrace();
            return "Erreur utilisateur";
        }
    }




    public Utilisateurs rechercherMemParEmail(String email) {
        String req = "SELECT * FROM membres WHERE Email = ?";
        try (PreparedStatement ps = connection.prepareStatement(req)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Utilisateurs utilisateur = new Utilisateurs();
                    utilisateur.setId(rs.getInt("id"));
                    utilisateur.setNom(rs.getString("Nom"));
                    utilisateur.setPrenom(rs.getString("Prénom"));
                    utilisateur.setEmail(rs.getString("Email"));
                    utilisateur.setCin(rs.getString("CIN"));
                    utilisateur.setAdresse(rs.getString("Adresse"));
                    utilisateur.setNumTel(rs.getString("NumTel"));
                    utilisateur.setRole(Role.valueOf(rs.getString("Role")));
                    utilisateur.setMotDePasse(rs.getString("motDePasse"));
                    return utilisateur;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String generatePasswordResetToken(String email) {
        String token = UUID.randomUUID().toString();
        Utilisateurs utilisateur = rechercherMemParEmail(email);
        if (utilisateur == null) return null;

        String sql = "INSERT INTO password_reset_tokens (user_id, token, expires_at) VALUES (?, ?, DATE_ADD(NOW(), INTERVAL 1 HOUR))";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, utilisateur.getId());
            ps.setString(2, token);
            ps.executeUpdate();
            return token;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Utilisateurs validateResetToken(String token) {
        String sql = "SELECT user_id FROM password_reset_tokens WHERE token = ? AND expires_at > NOW() AND used = FALSE";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, token);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int userId = rs.getInt("user_id");
                    Utilisateurs utilisateur = rechercherMem(userId);
                    if (utilisateur != null) {
                        String updateSql = "UPDATE password_reset_tokens SET used = TRUE WHERE token = ?";
                        try (PreparedStatement updatePs = connection.prepareStatement(updateSql)) {
                            updatePs.setString(1, token);
                            updatePs.executeUpdate();
                        }
                        return utilisateur;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void updatePassword(Utilisateurs utilisateur, String newPassword) {
        String sql = "UPDATE membres SET MotDePasse = ? WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, newPassword); // Should be hashed in production
            ps.setInt(2, utilisateur.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}