package fr.diginamic.hello.restControleurs;

import fr.diginamic.hello.entities.Ville;
import fr.diginamic.hello.services.VilleService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/villes")
public class VilleRestControleur {

    @Autowired
    private VilleService villeService;

    @GetMapping
    public List<Ville> getVilles() {
        return villeService.extractVilles();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Ville> getVilleParId(@PathVariable int id) {
        Ville ville = villeService.extractVille(id);
        if (ville == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(ville);
    }

    @GetMapping("/nom/{nom}")
    public ResponseEntity<Ville> getVilleParNom(@PathVariable String nom) {
        Ville ville = villeService.extractVille(nom);
        if (ville == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(ville);
    }

    @PostMapping
    public ResponseEntity<String> ajouterVille(@Valid @RequestBody Ville nouvelleVille, BindingResult result) {
        if (result.hasErrors()) {
            // Retourner un message d'erreur avec le code HTTP 400 si la validation échoue
            String errorMessage = result.getAllErrors()
                    .stream()
                    .map(ObjectError::getDefaultMessage)
                    .collect(Collectors.joining(", "));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erreur de validation : " + errorMessage);
        }

        // Vérifier si la ville existe déjà dans la base de données
        if (villeService.extractVille(nouvelleVille.getNom()) != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("La ville existe déjà.");
        }

        villeService.insertVille(nouvelleVille);
        return ResponseEntity.ok("Ville ajoutée avec succès.");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> modifierVille(@PathVariable int id, @Valid @RequestBody Ville villeModifiee, BindingResult result) {
        if (result.hasErrors()) {
            // Retourner un message d'erreur avec le code HTTP 400 si la validation échoue
            String errorMessage = result.getAllErrors()
                    .stream()
                    .map(ObjectError::getDefaultMessage)
                    .collect(Collectors.joining(", "));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erreur de validation : " + errorMessage);
        }

        // Vérifier si la ville existe dans la base de données
        Ville villeExistante = villeService.extractVille(id);
        if (villeExistante == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Ville non trouvée.");
        }

        // Mettre à jour la ville
        villeExistante.setNom(villeModifiee.getNom());
        villeExistante.setNbHabitants(villeModifiee.getNbHabitants());
        villeService.modifierVille(id, villeExistante);

        return ResponseEntity.ok("Ville modifiée avec succès.");
    }

    @DeleteMapping("/{id}")
    public List<Ville> supprimerVille(@PathVariable int id) {
        return villeService.supprimerVille(id);
    }
}
