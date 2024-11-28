package fr.diginamic.hello.restControleurs;

import fr.diginamic.hello.dto.DepartementDto;
import fr.diginamic.hello.dto.VilleDto;
import fr.diginamic.hello.entities.Departement;
import fr.diginamic.hello.entities.Ville;
import fr.diginamic.hello.mapper.DepartementMapper;
import fr.diginamic.hello.mapper.VilleMapper;
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

    @Autowired
    private DepartementMapper departementMapper;
    @Autowired
    private VilleMapper villeMapper;

    @GetMapping("/recherche/all")
    public ResponseEntity<List<DepartementDto>> getDepartements() {
        List<Departement> departements = departementService.extractAllDepartements();
        List<DepartementDto> departementsDto = departements.stream().map(departementMapper::toDto).toList();
        return ResponseEntity.ok(departementsDto);
    }

    @GetMapping("/recherche/{id}")
    public ResponseEntity<DepartementDto> getDepartementParId(@PathVariable int id) {
        Departement departement = departementService.extractDepartementParId(id);
        if (departement == null) {
            return ResponseEntity.notFound().build();
        }
        DepartementDto departementDto = departementMapper.toDto(departement);
        return ResponseEntity.ok(departementDto);
    }

    @GetMapping("/recherche/nom/{nom}")
    public ResponseEntity<DepartementDto> getDepartementByNom(@PathVariable String nom) {
        Departement departement = departementService.extractDepartementParNom(nom);
        if (departement == null) {
            return ResponseEntity.notFound().build();
        }
        DepartementDto departementDto = departementMapper.toDto(departement);
        return ResponseEntity.ok(departementDto);
    }

    @PostMapping
    public ResponseEntity<String> ajouterDepartement(@Valid @RequestBody DepartementDto nouveauDepartementDto, BindingResult result) {
        if (result.hasErrors()) {
            String errorMessage = result.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining(", "));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erreur de validation : " + errorMessage);
        }
        Departement nouveauDepartement = departementMapper.toEntity(nouveauDepartementDto);
        if (departementService.extractDepartementParNom(nouveauDepartement.getNom()) != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("La département existe déjà.");
        }
        departementService.insertDepartement(nouveauDepartement);
        return ResponseEntity.ok("Département ajouté avec succès.");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> modifierDepartement(@PathVariable int id, @Valid @RequestBody DepartementDto departementModifieDto, BindingResult result) {
        if (result.hasErrors()) {
            String errorMessage = result.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining(", "));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erreur de validation : " + errorMessage);
        }
        Departement departementExistant = departementService.extractDepartementParId(id);
        if (departementExistant == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Département non trouvée.");
        }
        Departement departementModifie=departementMapper.toEntity(departementModifieDto);
        departementService.modifierDepartement(id, departementModifie);
        return ResponseEntity.ok("Département modifié avec succès.");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimerDepartement(@PathVariable int id) {
        departementService.supprimerDepartement(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/recherche/{id}/villes/top/{n}")
    public ResponseEntity<List<VilleDto>> getTopNVilles(@PathVariable int id, @PathVariable int n) {
        Departement departement = departementService.extractDepartementParId(id);
        if (departement == null) {
            return ResponseEntity.notFound().build();
        }
        List<Ville> topVilles = departementService.extractTopNVillesParDepartement(id, n);
        List<VilleDto> topVillesDto = topVilles.stream().map(villeMapper::toDto).toList();
        return ResponseEntity.ok(topVillesDto);
    }

    @GetMapping("/recherche/{id}/villes/population")
    public ResponseEntity<List<VilleDto>> getVillesParPopulation(@PathVariable int id, @RequestParam int min, @RequestParam int max) {
        Departement departement = departementService.extractDepartementParId(id);
        if (departement == null) {
            return ResponseEntity.notFound().build();
        }
        List<Ville> villes = departementService.extractVillesEntreParDepartement(id, min, max);
        List<VilleDto> villesDto = villes.stream().map(villeMapper::toDto).toList();
        return ResponseEntity.ok(villesDto);
    }
}
