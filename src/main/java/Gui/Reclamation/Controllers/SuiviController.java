package Gui.Reclamation.Controllers;

import Models.Reclamation.Reclamation;
import Services.Reclamation.Crud.ReclamationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SuiviController {

    @Autowired
    private ReclamationService reclamationService;

    @GetMapping("/suivi")
    public String showSuivi(@RequestParam("reclam") int id, Model model) {
        try {
            long startTime = System.currentTimeMillis();
            System.out.println("Appel de la route /suivi avec ID : " + id);

            Reclamation reclamation = reclamationService.findById(id);
            if (reclamation == null) {
                System.out.println("Réclamation non trouvée pour l'ID : " + id);
                model.addAttribute("error", "Réclamation non trouvée");
                return "error";
            }
            System.out.println("Réclamation trouvée : " + reclamation.getTitre());
            System.out.println("ID : " + reclamation.getId());
            System.out.println("Titre : " + reclamation.getTitre());
            System.out.println("Description : " + reclamation.getDescription());
            System.out.println("Statut : " + (reclamation.getStatut() != null ? reclamation.getStatut().name() : "null"));
            model.addAttribute("reclamation", reclamation);

            long endTime = System.currentTimeMillis();
            System.out.println("Temps de traitement de /suivi : " + (endTime - startTime) + " ms");
            return "suivi";
        } catch (Exception e) {
            System.out.println("Erreur dans showSuivi : " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("error", "Une erreur s'est produite : " + e.getMessage());
            return "error";
        }
    }
}