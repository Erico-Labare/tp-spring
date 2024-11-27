package fr.diginamic.hello.services;

import fr.diginamic.hello.entities.Departement;
import fr.diginamic.hello.entities.Ville;
import fr.diginamic.hello.repository.DepartementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartementService {

    @Autowired
    private DepartementRepository departementRepository;

    public List<Departement> extractAllDepartements() {
        return departementRepository.findAllDepartements();
    }

    public Departement extractDepartementParId(int id) {
        return departementRepository.findById(id).orElse(null);
    }

    public Departement extractDepartementParNom(String nom) {
        return departementRepository.findByNom(nom).orElse(null);
    }

    public void insertDepartement(Departement departement) {
        departementRepository.save(departement);
    }

    public void modifierDepartement(int id, Departement departementModifie) {
        Departement departement = departementRepository.findById(id).orElse(null);
        if (departement != null) {
            departement.setNom(departementModifie.getNom());
            departement.setVilles(departementModifie.getVilles());
            departementRepository.save(departement);
        }
    }

    public void supprimerDepartement(int id) {
        departementRepository.deleteById(id);
    }

    public List<Ville> extractTopNVillesParDepartement(int departementId, int n) {
        return departementRepository.findTopNVillesByDepartement(departementId, n);
    }

    public List<Ville> extractVillesEntreParDepartement(int departementId, int min, int max) {
        return departementRepository.findVillesByPopulationRangeAndDepartement(departementId, min, max);
    }
}
