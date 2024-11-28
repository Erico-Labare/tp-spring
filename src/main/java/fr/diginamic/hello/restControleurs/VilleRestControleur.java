package fr.diginamic.hello.restControleurs;

import fr.diginamic.hello.dto.VilleDto;
import fr.diginamic.hello.entities.Ville;
import fr.diginamic.hello.exception.ControlerAdvice;
import fr.diginamic.hello.mapper.VilleMapper;
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
public class VilleRestControleur extends ControlerAdvice {

    @Autowired
    private VilleService villeService;

    @Autowired
    private VilleMapper villeMapper;

    @GetMapping("/recherche/all")
    public ResponseEntity<List<VilleDto>> getVilles() {
        List<Ville> villes = villeService.extractAllVilles();
        List<VilleDto> villesDto = villes.stream().map(villeMapper::toDto).toList();
        return ResponseEntity.ok(villesDto);
    }

    @GetMapping("/recherche/page")
    public ResponseEntity<Page<VilleDto>> getAllVilles(@RequestParam int page, @RequestParam int size) {
        Page<Ville> villes = villeService.extractAllVillesPage(page, size);
        Page<VilleDto> villesDto = villes.map(villeMapper::toDto);
        return ResponseEntity.ok(villesDto);
    }

    @GetMapping("/recherche/{id}")
    public ResponseEntity<VilleDto> getVilleParId(@PathVariable int id) {
        Ville ville = villeService.extractVilleParId(id);
        if (ville == null) {
            return ResponseEntity.notFound().build();
        }
        VilleDto villeDto = villeMapper.toDto(ville);
        return ResponseEntity.ok(villeDto);
    }

    @GetMapping("/recherche/nom/{nom}")
    public ResponseEntity<VilleDto> getVilleParNom(@PathVariable String nom) {
        Ville ville = villeService.extractVilleParNom(nom);
        if (ville == null) {
            return ResponseEntity.notFound().build();
        }
        VilleDto villeDto = villeMapper.toDto(ville);
        return ResponseEntity.ok(villeDto);
    }

    @PostMapping
    public ResponseEntity<String> ajouterVille(@Valid @RequestBody VilleDto  nouvelleVilleDto, BindingResult result) {
        if (result.hasErrors()) {
            String errorMessage = result.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining(", "));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erreur de validation : " + errorMessage);
        }

        Ville nouvelleVille = villeMapper.toEntity(nouvelleVilleDto);
        if (villeService.extractVilleParNom(nouvelleVille.getNom()) != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("La ville existe déjà.");
        }
        villeService.insertVille(nouvelleVille);
        return ResponseEntity.ok("Ville ajoutée avec succès.");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> modifierVille(@PathVariable int id, @Valid @RequestBody VilleDto villeModifieeDto, BindingResult result) {
        if (result.hasErrors()) {
            String errorMessage = result.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining(", "));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erreur de validation : " + errorMessage);
        }
        Ville villeExistante = villeService.extractVilleParId(id);
        if (villeExistante == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Ville non trouvée.");
        }
        Ville villeModifiee = villeMapper.toEntity(villeModifieeDto);
        villeService.modifierVille(id, villeModifiee);
        return ResponseEntity.ok("Ville modifiée avec succès.");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimerVille(@PathVariable int id) {
        villeService.supprimerVille(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/recherche/prefix")
    public ResponseEntity<List<VilleDto>> getParPrefixe(@RequestParam String prefix) {
        List<Ville> villes = villeService.extractVillesParPrefixe(prefix);
        List<VilleDto> villesDto = villes.stream().map(villeMapper::toDto).toList();
        return ResponseEntity.ok(villesDto);
    }

    @GetMapping("/recherche/population/supA")
    public ResponseEntity<List<VilleDto>> getPopulationSuperieureA(@RequestParam int min) {
        List<Ville> villes = villeService.extractVillesPopulationSup1(min);
        List<VilleDto> villesDto = villes.stream().map(villeMapper::toDto).toList();
        return ResponseEntity.ok(villesDto);
    }

    @GetMapping("/recherche/population/entre")
    public ResponseEntity<List<VilleDto>> getPopulationEntre(@RequestParam int min, @RequestParam int max) {
        List<Ville> villes = villeService.extractVillesPopulationEntre(min, max);
        List<VilleDto> villesDto = villes.stream().map(villeMapper::toDto).toList();
        return ResponseEntity.ok(villesDto);
    }

    @GetMapping("/recherche/departement/{id}/population/supA")
    public ResponseEntity<List<VilleDto>> getPopulationSupAParDepartement(@PathVariable int id, @RequestParam int min) {
        List<Ville> villes = villeService.extractVillesDepartementPopulationSupA(id, min);
        List<VilleDto> villesDto = villes.stream().map(villeMapper::toDto).toList();
        return ResponseEntity.ok(villesDto);
    }

    @GetMapping("/recherche/departement/{id}/population/entre")
    public ResponseEntity<List<VilleDto>> getPopulationEntreParDepartement(@PathVariable int id, @RequestParam int min, @RequestParam int max) {
        List<Ville> villes = villeService.extractVillesDepartementPopulationEntre(id, min, max);
        List<VilleDto> villesDto = villes.stream().map(villeMapper::toDto).toList();
        return ResponseEntity.ok(villesDto);
    }

    @GetMapping("/recherche/departement/{id}/top")
    public ResponseEntity<List<VilleDto>> getTopNVilles(@PathVariable int id, @RequestParam int n) {
        List<Ville> villes = villeService.extractTopNVillesDepartement(id, n);
        List<VilleDto> villesDto = villes.stream().map(villeMapper::toDto).toList();
        return ResponseEntity.ok(villesDto);
    }
}
