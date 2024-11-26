package fr.diginamic.hello.services;

import fr.diginamic.hello.dao.DepartementDao;
import fr.diginamic.hello.dao.VilleDao;
import fr.diginamic.hello.entities.Departement;
import fr.diginamic.hello.entities.Ville;
import fr.diginamic.hello.repository.DepartementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartementService {

    @Autowired
    private DepartementDao departementDao;

    @Autowired
    private VilleDao villeDao;

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

    public List<Departement> supprimerDepartement(int id) {
        departementRepository.deleteById(id);
        return departementRepository.findAll();
    }

    public List<Ville> getTopNVillesByPopulation(Departement departement, int n) {
        return departement.getVilles().stream().sorted((v1, v2) -> Integer.compare(v2.getNbHabitants(), v1.getNbHabitants())).limit(n).toList();
    }

    public List<Ville> getVillesParPopulation(Departement departement, int min, int max) {
        return departement.getVilles().stream().filter(ville -> ville.getNbHabitants() >= min && ville.getNbHabitants() <= max).toList();
    }
}
