package Gui.Reservation.Controllers;

import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.draw.SolidLine;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.LineSeparator;
import com.itextpdf.layout.element.Image;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.itextpdf.layout.properties.HorizontalAlignment;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class PdfTemplateUtil {

    /**
     * Génère un PDF avec un design standardisé, incluant un logo optionnel.
     *
     * @param filePath Chemin où sauvegarder le PDF
     * @param title Titre du PDF
     * @param data Données à afficher dans le tableau (Map avec clé = nom du champ, valeur = détail)
     * @param logoPath Chemin vers le logo (optionnel, peut être null si aucun logo n'est requis)
     * @throws IOException En cas d'erreur lors de la génération du PDF
     */
    public static void generatePdf(String filePath, String title, List<Map<String, String>> data, String logoPath) throws IOException {
        PdfWriter writer = new PdfWriter(filePath);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        // Ajouter un logo si le chemin est fourni
        if (logoPath != null && !logoPath.isEmpty()) {
            try {
                Image logo = new Image(ImageDataFactory.create(PdfTemplateUtil.class.getResource(logoPath)));
                logo.setHorizontalAlignment(HorizontalAlignment.CENTER);
                logo.scaleToFit(100, 100); // Ajuster la taille du logo (largeur, hauteur)
                logo.setMarginBottom(20);
                document.add(logo);
            } catch (Exception e) {
                // Si le logo ne peut pas être chargé, on continue sans lui
                System.err.println("Erreur lors du chargement du logo: " + e.getMessage());
            }
        }

        // Titre avec alignement et style
        Paragraph titleParagraph = new Paragraph(title);
        titleParagraph.setFontSize(20);
        titleParagraph.setBold();
        titleParagraph.setTextAlignment(TextAlignment.CENTER);
        titleParagraph.setMarginBottom(20);
        document.add(titleParagraph);

        // Ajouter une ligne séparatrice
        LineSeparator separator = new LineSeparator(new SolidLine(1f));
        separator.setMarginBottom(15);
        document.add(separator);

        // Créer un tableau pour organiser les détails
        Table table = new Table(new float[]{1, 3}); // Deux colonnes avec proportions
        table.setWidth(UnitValue.createPercentValue(100));
        table.setMarginBottom(20);

        // Définir les couleurs
        DeviceRgb headerColor = new DeviceRgb(200, 220, 255);
        DeviceRgb rowColor = new DeviceRgb(240, 240, 240);

        // Ajouter les en-têtes du tableau
        Cell headerField = new Cell().add(new Paragraph("Field"));
        headerField.setBackgroundColor(headerColor);
        headerField.setFontSize(12);
        headerField.setBold();
        headerField.setPadding(5);
        headerField.setTextAlignment(TextAlignment.LEFT);
        table.addCell(headerField);

        Cell headerDetails = new Cell().add(new Paragraph("Details"));
        headerDetails.setBackgroundColor(headerColor);
        headerDetails.setFontSize(12);
        headerDetails.setBold();
        headerDetails.setPadding(5);
        headerDetails.setTextAlignment(TextAlignment.LEFT);
        table.addCell(headerDetails);

        // Ajouter les données dans le tableau
        for (Map<String, String> rowData : data) {
            for (Map.Entry<String, String> entry : rowData.entrySet()) {
                Cell fieldCell = new Cell().add(new Paragraph(entry.getKey()));
                fieldCell.setBackgroundColor(rowColor);
                fieldCell.setFontSize(11);
                fieldCell.setPadding(5);
                fieldCell.setTextAlignment(TextAlignment.LEFT);
                table.addCell(fieldCell);

                Cell valueCell = new Cell().add(new Paragraph(entry.getValue()));
                valueCell.setBackgroundColor(rowColor);
                valueCell.setFontSize(11);
                valueCell.setPadding(5);
                valueCell.setTextAlignment(TextAlignment.LEFT);
                table.addCell(valueCell);
            }
        }

        document.add(table);

        // Ajouter un footer avec timestamp
        Paragraph footer = new Paragraph("Generated on: " + new Date().toString());
        footer.setFontSize(10);
        footer.setItalic();
        footer.setTextAlignment(TextAlignment.CENTER);
        footer.setMarginTop(20);
        document.add(footer);

        // Fermer le document
        document.close();
    }
}