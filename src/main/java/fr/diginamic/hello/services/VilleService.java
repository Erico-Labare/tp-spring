package fr.diginamic.hello.services;

import fr.diginamic.hello.dao.VilleDao;
import fr.diginamic.hello.entities.Ville;
import fr.diginamic.hello.repository.VilleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VilleService {

    @Autowired
    private VilleDao villeDao;

    @Autowired
    private VilleRepository villeRepository;


    public List<Ville> extractAllVilles() {
        return villeDao.extractVilles();
    }

    public Page<Ville> getAllVilles(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return villeRepository.findAll(pageable);
    }

    public Ville extractVilleParId(int idVille) {
        return villeDao.extractVilleById(idVille);
    }

    public Ville extractVilleParNom(String nom) {
        return villeDao.extractVilleByName(nom);
    }

    public void insertVille(Ville ville) {
        villeDao.insertVille(ville);
        extractAllVilles();
    }

    public void modifierVille(int idVille, Ville villeModifiee) {
        Ville villeExistante = villeDao.extractVilleById(idVille);
        if (villeExistante != null) {
            villeExistante.setNom(villeModifiee.getNom());
            villeExistante.setNbHabitants(villeModifiee.getNbHabitants());
            if (villeModifiee.getDepartement() != null) {
                villeExistante.setDepartement(villeModifiee.getDepartement());
            }
            villeDao.updateVille(villeExistante);
        }
    }

    public List<Ville> supprimerVille(int idVille) {
        villeDao.deleteVille(idVille);
        return extractAllVilles();
    }

    public List<Ville> rechercherVillesParPrefixe(String prefix) {
        return villeRepository.findByNomStartingWith(prefix);
    }

    public List<Ville> rechercherVillesPopulationSup1(int min) {
        return villeRepository.findByNbHabitantsGreaterThan(min);
    }

    public List<Ville> rechercherVillesPopulationEntre(int min, int max) {
        return villeRepository.findByNbHabitantsBetween(min, max);
    }

    public List<Ville> villesDepartementPopulationSupA(int departementId, int min) {
        return villeRepository.findByDepartementAndPopulationGreaterThan(departementId, min);
    }

    public List<Ville> villesDepartementPopulationEntre(int departementId, int min, int max) {
        return villeRepository.findByDepartementAndPopulationBetween(departementId, min, max);
    }

    public List<Ville> topNVillesDepartement(int departementId, int n) {
        Pageable pageable = PageRequest.of(0, n);
        return villeRepository.findTopNVillesByDepartement(departementId, pageable);
    }
}