package fr.diginamic.hello.viewController;

import fr.diginamic.hello.services.DepartementService;
import fr.diginamic.hello.services.VilleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class VilleViewController {

    @Autowired
    private VilleService villeService;
    @Autowired
    private DepartementService departementService;

    @GetMapping("/listVilles")
    public String getVilles(Model model) {
        model.addAttribute("villes", villeService.extractAllVilles());
        model.addAttribute("departement", departementService.extractAllDepartements());
        return "villes/listVilles";
    }

    @GetMapping("/supprimerVille/{id}")
    public String deleteVille(@PathVariable int id) {
        villeService.supprimerVille(id);
        return "redirect:/listVilles";
    }
}
