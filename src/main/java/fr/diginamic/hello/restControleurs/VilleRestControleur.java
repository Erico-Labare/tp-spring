package fr.diginamic.hello.restControleurs;

import fr.diginamic.hello.entities.Ville;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/villes")
public class VilleRestControleur {

    @GetMapping
    public List<Ville> getVilles() {
        List<Ville> villes = new ArrayList<>();
        villes.add(new Ville("Paris", 2148000));
        villes.add(new Ville("Lyon", 513000));
        villes.add(new Ville("Marseille", 861000));
        villes.add(new Ville("Toulouse", 479000));
        return villes;
    }
}
