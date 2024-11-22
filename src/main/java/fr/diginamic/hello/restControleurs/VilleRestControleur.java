package fr.diginamic.hello.restControleurs;

import fr.diginamic.hello.entities.Ville;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/villes")
public class VilleRestControleur {

    private final List<Ville> villes = new ArrayList<>();

    public VilleRestControleur() {
        villes.add(new Ville("Paris", 2148000));
        villes.add(new Ville("Lyon", 513000));
        villes.add(new Ville("Marseille", 861000));
        villes.add(new Ville("Toulouse", 479000));
    }

    @GetMapping
    public List<Ville> getVilles() {
        return villes;
    }

    @PostMapping
    public ResponseEntity<String> ajouterVille(@RequestBody Ville nouvelleVille) {
        boolean existeDeja = villes.stream().anyMatch(ville -> ville.getNom().equalsIgnoreCase(nouvelleVille.getNom()));
        if (existeDeja) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("La ville existe déjà");
        }
        villes.add(nouvelleVille);
        return ResponseEntity.ok("Ville insérée avec succès");
    }
}
