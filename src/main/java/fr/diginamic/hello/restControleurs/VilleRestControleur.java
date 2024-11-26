package fr.diginamic.hello.restControleurs;

import fr.diginamic.hello.entities.Ville;
import fr.diginamic.hello.services.VilleService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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

    @GetMapping("/recherche/all")
    public List<Ville> getVilles() {
        return villeService.extractAllVilles();
    }

    @GetMapping("/recherche")
    public ResponseEntity<Page<Ville>> getAllVilles(@RequestParam int page, @RequestParam int size) {
        Page<Ville> villes = villeService.getAllVilles(page, size);
        return ResponseEntity.ok(villes);
    }

    @GetMapping("/recherche/{id}")
    public ResponseEntity<Ville> getVilleParId(@PathVariable int id) {
        Ville ville = villeService.extractVilleParId(id);
        if (ville == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(ville);
    }

    @GetMapping("/recherche/nom/{nom}")
    public ResponseEntity<Ville> getVilleParNom(@PathVariable String nom) {
        Ville ville = villeService.extractVilleParNom(nom);
        if (ville == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(ville);
    }

    @PostMapping
    public ResponseEntity<String> ajouterVille(@Valid @RequestBody Ville nouvelleVille, BindingResult result) {
        if (result.hasErrors()) {
            String errorMessage = result.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining(", "));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erreur de validation : " + errorMessage);
        }

        if (villeService.extractVilleParNom(nouvelleVille.getNom()) != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("La ville existe déjà.");
        }

        villeService.insertVille(nouvelleVille);
        return ResponseEntity.ok("Ville ajoutée avec succès.");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> modifierVille(@PathVariable int id, @Valid @RequestBody Ville villeModifiee, BindingResult result) {
        if (result.hasErrors()) {
            String errorMessage = result.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining(", "));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erreur de validation : " + errorMessage);
        }

        Ville villeExistante = villeService.extractVilleParId(id);
        if (villeExistante == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Ville non trouvée.");
        }

        villeService.modifierVille(id, villeModifiee);
        return ResponseEntity.ok("Ville modifiée avec succès.");
    }

    @DeleteMapping("/{id}")
    public List<Ville> supprimerVille(@PathVariable int id) {
        return villeService.supprimerVille(id);
    }

    @GetMapping("/recherche/prefix")
    public ResponseEntity<List<Ville>> rechercherParPrefixe(@RequestParam String prefix) {
        List<Ville> villes = villeService.rechercherVillesParPrefixe(prefix);
        return ResponseEntity.ok(villes);
    }

    @GetMapping("/recherche/population/supA")
    public ResponseEntity<List<Ville>> populationSuperieureA(@RequestParam int min) {
        List<Ville> villes = villeService.rechercherVillesPopulationSup1(min);
        return ResponseEntity.ok(villes);
    }

    @GetMapping("/recherche/population/entre")
    public ResponseEntity<List<Ville>> populationEntre(@RequestParam int min, @RequestParam int max) {
        List<Ville> villes = villeService.rechercherVillesPopulationEntre(min, max);
        return ResponseEntity.ok(villes);
    }

    @GetMapping("/recherche/departement/{id}/population/supA")
    public ResponseEntity<List<Ville>> populationSupAParDepartement(@PathVariable int id, @RequestParam int min) {
        List<Ville> villes = villeService.villesDepartementPopulationSupA(id, min);
        return ResponseEntity.ok(villes);
    }

    @GetMapping("/recherche/departement/{id}/population/entre")
    public ResponseEntity<List<Ville>> populationEntreParDepartement(@PathVariable int id, @RequestParam int min, @RequestParam int max) {
        List<Ville> villes = villeService.villesDepartementPopulationEntre(id, min, max);
        return ResponseEntity.ok(villes);
    }

    @GetMapping("/recherche/departement/{id}/top")
    public ResponseEntity<List<Ville>> topNVilles(@PathVariable int id, @RequestParam int n) {
        List<Ville> villes = villeService.topNVillesDepartement(id, n);
        return ResponseEntity.ok(villes);
    }
}
