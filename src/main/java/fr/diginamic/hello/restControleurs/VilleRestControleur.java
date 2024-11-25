package fr.diginamic.hello.restControleurs;

import fr.diginamic.hello.entities.Ville;
import fr.diginamic.hello.services.VilleService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/villes")
public class VilleRestControleur {

    @Autowired
    private VilleService villeService;

    @GetMapping
    public List<Ville> getVilles() {
        return villeService.getListVille();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Ville> getVilleParId(@PathVariable int id) {
        Ville ville = villeService.getVilleById(id);
        if (ville == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(ville);
    }

    @PostMapping
    public ResponseEntity<String> ajouterVille(@Valid @RequestBody Ville nouvelleVille, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erreur : " + result.getFieldError().getDefaultMessage());
        }
        if (!villeService.ajouterVille(nouvelleVille)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Une ville avec cet ID existe déjà.");
        }
        return ResponseEntity.ok("Ville ajoutée avec succès.");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> modifierVille(@PathVariable int id, @Valid @RequestBody Ville villeModifiee, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erreur : " + result.getFieldError().getDefaultMessage());
        }
        if (!villeService.modifierVille(id, villeModifiee)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Ville non trouvée.");
        }
        return ResponseEntity.ok("Ville modifiée avec succès.");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> supprimerVille(@PathVariable int id) {
        if (!villeService.supprimerVille(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Ville non trouvée.");
        }
        return ResponseEntity.ok("Ville supprimée avec succès.");
    }
}
