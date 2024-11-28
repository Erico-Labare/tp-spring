package fr.diginamic.hello.restControleurs;

import fr.diginamic.hello.dto.VilleDto;
import fr.diginamic.hello.entities.Departement;
import fr.diginamic.hello.entities.Ville;
import fr.diginamic.hello.exception.ControlerAdvice;
import fr.diginamic.hello.exception.FunctionalException;
import fr.diginamic.hello.mapper.VilleMapper;
import fr.diginamic.hello.services.DepartementService;
import fr.diginamic.hello.services.VilleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/villes")
public class VilleRestControleur extends ControlerAdvice {

    @Autowired
    private VilleService villeService;

    @Autowired
    private DepartementService departementService;

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

    @Operation(summary = "Création d'une nouvelle ville", description = "Ajoute une nouvelle ville à la base de données.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Ville ajoutée avec succès.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class)) }),
            @ApiResponse(responseCode = "400",
                    description = "Erreur de validation ou règle métier non respectée.",
                    content = @Content)
    })
    @PostMapping
    public ResponseEntity<String> ajouterVille(@Valid @RequestBody VilleDto  nouvelleVilleDto, BindingResult result) throws FunctionalException {
        if (result.hasErrors()) {
            String errorMessage = result.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining(", "));
            throw new FunctionalException("Erreur de validation : " + errorMessage);
        }

        villeService.validateVille(nouvelleVilleDto);

        Ville nouvelleVille = villeMapper.toEntity(nouvelleVilleDto);
        if (villeService.extractVilleParNom(nouvelleVille.getNom()) != null) {
            throw new FunctionalException("La ville existe déjà.");
        }
        villeService.insertVille(nouvelleVille);
        return ResponseEntity.ok("Ville ajoutée avec succès.");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> modifierVille(@PathVariable int id, @Valid @RequestBody VilleDto villeModifieeDto, BindingResult result) throws FunctionalException {
        if (result.hasErrors()) {
            String errorMessage = result.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining(", "));
            throw new FunctionalException("Erreur de validation : " + errorMessage);
        }

        villeService.validateVille(villeModifieeDto);

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
    public ResponseEntity<List<VilleDto>> getParPrefixe(@RequestParam String prefix) throws FunctionalException {
        List<Ville> villes = villeService.extractVillesParPrefixe(prefix);
        if (villes.isEmpty()) {
            throw new FunctionalException("Aucune ville dont le nom commence par " + prefix + " n’a été trouvée.");
        }
        List<VilleDto> villesDto = villes.stream().map(villeMapper::toDto).toList();
        return ResponseEntity.ok(villesDto);
    }

    @GetMapping("/recherche/population/supA")
    public ResponseEntity<List<VilleDto>> getPopulationSuperieureA(@RequestParam int min) throws FunctionalException {
        List<Ville> villes = villeService.extractVillesPopulationSup1(min);
        if (villes.isEmpty()) {
            throw new FunctionalException("Aucune ville n’a une population supérieure à " + min + ".");
        }
        List<VilleDto> villesDto = villes.stream().map(villeMapper::toDto).toList();
        return ResponseEntity.ok(villesDto);
    }

    @GetMapping("/recherche/population/supAExport")
    public ResponseEntity<String> getPopulationSuperieureAExport(@RequestParam int min, HttpServletResponse response) throws FunctionalException, IOException {
       List<Ville> villes = villeService.extractVillesPopulationSup1(min);
        if (villes.isEmpty()) {
            throw new FunctionalException("Aucune ville n’a une population supérieure à " + min + ".");
        }
        List<VilleDto> villesDto = villes.stream().map(villeMapper::toDto).toList();

        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=\"villes.csv\"");

        PrintWriter writer = response.getWriter();

        for (VilleDto villeDto : villesDto) {
            String codeDept = villeDto.getCodeDepartement();
            String nomDept = departementService.getNomDepartement(codeDept);
            String csvLine = String.format("%s,%d,%s,%s",
                    villeDto.getNomVille(),
                    villeDto.getNbHabitants(),
                    codeDept,
                    nomDept);

            writer.println(csvLine);
        }

        writer.flush();

        return ResponseEntity.ok("CVS créer.");
    }

    @GetMapping("/recherche/population/entre")
    public ResponseEntity<List<VilleDto>> getPopulationEntre(@RequestParam int min, @RequestParam int max) throws FunctionalException {
        List<Ville> villes = villeService.extractVillesPopulationEntre(min, max);
        if (villes.isEmpty()) {
            throw new FunctionalException("Aucune ville n’a une population comprise entre " + min + " et " + max + ".");
        }
        List<VilleDto> villesDto = villes.stream().map(villeMapper::toDto).toList();
        return ResponseEntity.ok(villesDto);
    }

    @GetMapping("/recherche/departement/{id}/population/supA")
    public ResponseEntity<List<VilleDto>> getPopulationSupAParDepartement(@PathVariable int id, @RequestParam int min) throws FunctionalException {
        List<Ville> villes = villeService.extractVillesDepartementPopulationSupA(id, min);
        if (villes.isEmpty()) {
            throw new FunctionalException("Aucune ville n’a une population supérieure à " + min + ".");
        }
        List<VilleDto> villesDto = villes.stream().map(villeMapper::toDto).toList();
        return ResponseEntity.ok(villesDto);
    }

    @GetMapping("/recherche/departement/{id}/population/entre")
    public ResponseEntity<List<VilleDto>> getPopulationEntreParDepartement(@PathVariable int id, @RequestParam int min, @RequestParam int max) throws FunctionalException {
        List<Ville> villes = villeService.extractVillesDepartementPopulationEntre(id, min, max);
        if (villes.isEmpty()) {
            throw new FunctionalException("Aucune ville n’a une population comprise entre " + min + " et " + max + ".");
        }
        List<VilleDto> villesDto = villes.stream().map(villeMapper::toDto).toList();
        return ResponseEntity.ok(villesDto);
    }

    @GetMapping("/recherche/departement/{id}/top")
    public ResponseEntity<List<VilleDto>> getTopNVilles(@PathVariable int id, @RequestParam int n) throws FunctionalException {
        List<Ville> villes = villeService.extractTopNVillesDepartement(id, n);
        if (villes.isEmpty()) {
            throw new FunctionalException("Aucune ville trouvée pour le département.");
        }
        List<VilleDto> villesDto = villes.stream().map(villeMapper::toDto).toList();
        return ResponseEntity.ok(villesDto);
    }
}
