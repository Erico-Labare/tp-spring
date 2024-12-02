package fr.diginamic.hello.services;

import fr.diginamic.hello.dto.VilleDto;
import fr.diginamic.hello.entities.Departement;
import fr.diginamic.hello.entities.Ville;
import fr.diginamic.hello.exception.FunctionalException;
import fr.diginamic.hello.mapper.VilleMapper;
import fr.diginamic.hello.repository.DepartementRepository;
import fr.diginamic.hello.repository.VilleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.util.AssertionErrors.assertEquals;

@SpringBootTest
@ActiveProfiles("test")
public class VilleServiceTest {

    @Autowired
    private VilleService villeService;

    @MockitoBean
    private VilleRepository villeRepository;

    @MockitoBean
    private DepartementRepository departementRepository;

    private Departement departement;
    private Ville ville;
    private Ville ville2;
    private List<Ville> villes;
    VilleDto villeDto;

    @Autowired
    private VilleMapper villeMapper;

    @BeforeEach
    public void ini() {
        departement = new Departement();
        departement.setId(1);
        departement.setCode("34");
        departement.setNom("Hérault");

        ville = new Ville();
        ville.setId(1);
        ville.setNom("Montpellier");
        ville.setNbHabitants(277639);
        ville.setDepartement(departement);

        ville2 = new Ville();
        ville2.setId(2);
        ville2.setNom("Béziers");
        ville2.setNbHabitants(75000);
        ville2.setDepartement(departement);

        villes = new ArrayList<>();
        villes.add(ville);
        villes.add(ville2);
    }


    @Test
    public void testExtractAllVilles() {

        Mockito.when(villeRepository.findAll()).thenReturn(villes);

        List<Ville> resultat = villeService.extractAllVilles();

        assertNotNull(resultat, "La liste des villes ne doit pas être nulle.");
        assertEquals("La liste doit contenir exactement 2 villes.", 2, resultat.size());
        assertEquals("La première ville doit être Montpellier.", "Montpellier", resultat.get(0).getNom());
        assertEquals("La deuxième ville doit être Béziers.", "Béziers", resultat.get(1).getNom());


    }

    @Test
    public void testExtractVilleParId() {
        Mockito.when(villeRepository.findById(1)).thenReturn(Optional.of(ville));

        Ville result = villeService.extractVilleParId(1);

        assertNotNull(result, "La ville ne doit pas être nulle.");
        assertEquals("La ville doit être Montpellier.", "Montpellier", result.getNom());
    }

    @Test
    public void testExtractVilleParIdNotFound() {
        Mockito.when(villeRepository.findById(99)).thenReturn(Optional.empty());

        Ville result = villeService.extractVilleParId(99);

        assertNull(result, "La ville doit être nulle si l'ID n'existe pas.");
    }

    @Test
    public void testInsertVille() {
        Mockito.when(departementRepository.findByCode("34")).thenReturn(Optional.of(departement));
        Mockito.when(villeRepository.save(ville)).thenReturn(ville);

        villeService.insertVille(ville);

        Mockito.verify(villeRepository, Mockito.times(1)).save(ville);
    }

    @Test
    public void testInsertVilleWithoutDepartement() {
        Mockito.when(departementRepository.findByCode("99")).thenReturn(Optional.empty());

        Ville villeSansDepartement = new Ville();
        villeSansDepartement.setNom("VilleSansDep");
        villeSansDepartement.setNbHabitants(100);
        villeSansDepartement.setDepartement(new Departement());
        villeSansDepartement.getDepartement().setCode("99");

        villeService.insertVille(villeSansDepartement);

        Mockito.verify(villeRepository, Mockito.never()).save(Mockito.any());
    }

    @Test
    public void testModifierVille() {
        Mockito.when(villeRepository.findById(1)).thenReturn(Optional.of(ville));
        Mockito.when(departementRepository.findById(1)).thenReturn(Optional.of(departement));

        Ville villeModifiee = new Ville();
        villeModifiee.setNom("MontpellierModifiee");
        villeModifiee.setNbHabitants(280000);
        villeModifiee.setDepartement(departement);

        villeService.modifierVille(1, villeModifiee);

        Mockito.verify(villeRepository, Mockito.times(1)).save(ville);
        assertEquals("Le nom de la ville doit être mis à jour.", "MontpellierModifiee", ville.getNom());
        assertEquals("Le nombre d'habitants doit être mis à jour.", 280000, ville.getNbHabitants());
    }

    @Test
    public void testSupprimerVille() {
        villeService.supprimerVille(1);

        Mockito.verify(villeRepository, Mockito.times(1)).deleteById(1);
    }

    @Test
    public void testExtractVillesPopulationSup1() {
        Mockito.when(villeRepository.findByNbHabitantsGreaterThan(100000)).thenReturn(List.of(ville));

        List<Ville> result = villeService.extractVillesPopulationSup1(100000);

        assertNotNull(result, "La liste des villes ne doit pas être nulle.");
        assertEquals("La liste doit contenir exactement 1 ville.", 1, result.size());
        assertEquals("La ville doit être Montpellier.", "Montpellier", result.get(0).getNom());
    }

    @Test
    public void testExtractVillesParPrefixe() {
        Mockito.when(villeRepository.findByNomStartingWith("Mont")).thenReturn(List.of(ville));

        List<Ville> result = villeService.extractVillesParPrefixe("Mont");

        assertNotNull(result, "La liste des villes ne doit pas être nulle.");
        assertEquals("La liste doit contenir exactement 1 ville.", 1, result.size());
        assertEquals("La ville doit être Montpellier.", "Montpellier", result.get(0).getNom());
    }

    @Test
    public void testValidateVille() throws FunctionalException {
        villeDto = villeMapper.toDto(ville);
        assertDoesNotThrow(() -> villeService.validateVille(villeDto), "La validation ne doit pas lever d'exception pour des données valides.");

        villeDto.setNbHabitants(5);
        FunctionalException exception = assertThrows(FunctionalException.class,() -> villeService.validateVille(villeDto));
        assertEquals("Le message d'erreur attendu pour une population insuffisante n'est pas correct.", "FunctionalException : La ville doit avoir au moins 10 habitants.", exception.getMessage());

        villeDto.setNomVille("M");
        villeDto.setNbHabitants(50000);
        FunctionalException exception2 = assertThrows(FunctionalException.class,() -> villeService.validateVille(villeDto));
        assertEquals("Le message d'erreur attendu pour un nom de ville trop court n'est pas correct.","FunctionalException : Le nom de la ville doit contenir au moins 2 lettres.", exception2.getMessage());

        villeDto.setNomVille("Montpellier");
        villeDto.setCodeDepartement("3");
        FunctionalException exception3 = assertThrows(FunctionalException.class,() -> villeService.validateVille(villeDto));
        assertEquals("Le message d'erreur attendu pour un code département incorrect n'est pas correct.","FunctionalException : Le code département doit contenir exactement 2 caractères.", exception3.getMessage());

        villeDto.setNomVille(null);
        villeDto.setCodeDepartement("34");
        FunctionalException exceptionNom = assertThrows(FunctionalException.class,() -> villeService.validateVille(villeDto));
        assertEquals("Le message d'erreur attendu pour un nom nul n'est pas correct.","FunctionalException : Le nom de la ville doit contenir au moins 2 lettres.", exceptionNom.getMessage());

        villeDto.setNomVille("Monpelllier");
        villeDto.setCodeDepartement(null);
        FunctionalException exceptionCode = assertThrows(FunctionalException.class,() -> villeService.validateVille(villeDto));
        assertEquals("Le message d'erreur attendu pour un code département nul n'est pas correct.","FunctionalException : Le code département doit contenir exactement 2 caractères.", exceptionCode.getMessage());
    }


}
