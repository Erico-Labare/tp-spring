package fr.diginamic.hello.services;

import fr.diginamic.hello.entities.Departement;
import fr.diginamic.hello.entities.Ville;
import fr.diginamic.hello.repository.DepartementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DepartementService {

    @Autowired
    private DepartementRepository departementRepository;

    @Transactional
    public List<Departement> extractAllDepartements() {
        return departementRepository.findAllDepartements();
    }

    @Transactional
    public Departement extractDepartementParId(int id) {
        return departementRepository.findById(id).orElse(null);
    }

    @Transactional
    public Departement extractDepartementParNom(String nom) {
        return departementRepository.findByNom(nom).orElse(null);
    }

    @Transactional
    public void insertDepartement(Departement departement) {
        departementRepository.save(departement);
    }

    @Transactional
    public void modifierDepartement(int id, Departement departementModifie) {
        Departement departement = departementRepository.findById(id).orElse(null);
        if (departement != null) {
            departement.setNom(departementModifie.getNom());
            departement.setVilles(departementModifie.getVilles());
            departementRepository.save(departement);
        }
    }

    @Transactional
    public void supprimerDepartement(int id) {
        departementRepository.deleteById(id);
    }

    @Transactional
    public List<Ville> extractTopNVillesParDepartement(int departementId, int n) {
        return departementRepository.findTopNVillesByDepartement(departementId, n);
    }

    @Transactional
    public List<Ville> extractVillesEntreParDepartement(int departementId, int min, int max) {
        return departementRepository.findVillesByPopulationRangeAndDepartement(departementId, min, max);
    }
}
