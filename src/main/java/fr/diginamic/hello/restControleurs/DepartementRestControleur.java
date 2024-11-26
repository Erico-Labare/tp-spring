package fr.diginamic.hello.restControleurs;

import fr.diginamic.hello.entities.Departement;
import fr.diginamic.hello.entities.Ville;
import fr.diginamic.hello.services.DepartementService;
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
@RequestMapping("/departement")
public class DepartementRestControleur {

    @Autowired
    private DepartementService departementService;

    @GetMapping
    public List<Departement> getDepartements() {
        return departementService.extractAllDepartements();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Departement> getDepartementParId(@PathVariable int id) {
        Departement departement = departementService.extractDepartementParId(id);
        if (departement == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(departement);
    }

    @PostMapping
    public ResponseEntity<String> ajouterDepartement(@Valid @RequestBody Departement nouveauDepartement, BindingResult result) {
        if (result.hasErrors()) {
            String errorMessage = result.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining(", "));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erreur de validation : " + errorMessage);
        }

        if (departementService.extractDepartementParNom(nouveauDepartement.getNom()) != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("La département existe déjà.");
        }

        departementService.insertDepartement(nouveauDepartement);
        return ResponseEntity.ok("Département ajouté avec succès.");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> modifierDepartement(@PathVariable int id, @Valid @RequestBody Departement departementModifie, BindingResult result) {
        if (result.hasErrors()) {
            String errorMessage = result.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining(", "));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erreur de validation : " + errorMessage);
        }

        Departement departementExistant = departementService.extractDepartementParId(id);
        if (departementExistant == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Département non trouvée.");
        }

        departementService.modifierDepartement(id, departementModifie);
        return ResponseEntity.ok("Département modifié avec succès.");
    }

    @DeleteMapping("/{id}")
    public List<Departement> supprimerDepartement(@PathVariable int id) {
        return departementService.supprimerDepartement(id);
    }

    @GetMapping("/{id}/villes/top/{n}")
    public ResponseEntity<List<Ville>> getTopNVilles(@PathVariable int id, @PathVariable int n) {
        Departement departement = departementService.extractDepartementParId(id);
        if (departement == null) {
            return ResponseEntity.notFound().build();
        }
        List<Ville> topVilles = departementService.getTopNVillesByPopulation(departement, n);
        return ResponseEntity.ok(topVilles);
    }

    @GetMapping("/{id}/villes/population")
    public ResponseEntity<List<Ville>> getVillesParPopulation(@PathVariable int id, @RequestParam int min, @RequestParam int max) {
        Departement departement = departementService.extractDepartementParId(id);
        if (departement == null) {
            return ResponseEntity.notFound().build();
        }

        List<Ville> villes = departementService.getVillesParPopulation(departement, min, max);
        return ResponseEntity.ok(villes);
    }
}
