package fr.diginamic.hello.services;

import fr.diginamic.hello.dao.DepartementDao;
import fr.diginamic.hello.dao.VilleDao;
import fr.diginamic.hello.entities.Departement;
import fr.diginamic.hello.entities.Ville;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartementService {

    @Autowired
    private DepartementDao departementDao;

    @Autowired
    private VilleDao villeDao;

    public List<Departement> extractAllDepartements() {
        return departementDao.extractDepartements();
    }

    public Departement extractDepartementParId(int id) {
        return departementDao.extractDepartementById(id);
    }

    public Departement extractDepartementParNom(String nom) {
        return departementDao.extractDepartementByName(nom);
    }

    public List<Departement> insertDepartement(Departement departement) {
        departementDao.insertDepartement(departement);
        return extractAllDepartements();
    }

    public List<Departement> modifierDepartement(int id, Departement departementModifie) {
        Departement departement = departementDao.extractDepartementById(id);
        if (departement != null) {
            departement.setNom(departementModifie.getNom());
            departement.setVilles(departementModifie.getVilles());
            departementDao.updateDepartement(departement);
        }
        return extractAllDepartements();
    }

    public List<Departement> supprimerDepartement(int id) {
        departementDao.deleteDepartement(id);
        return extractAllDepartements();
    }

    public List<Ville> getTopNVillesByPopulation(Departement departement, int n) {
        return departement.getVilles().stream()
                .sorted((v1, v2) -> Integer.compare(v2.getNbHabitants(), v1.getNbHabitants())) // Tri par population décroissante
                .limit(n) // Limite au top N
                .toList();
    }

    public List<Ville> getVillesByPopulationRange(Departement departement, int min, int max) {
        return departement.getVilles().stream()
                .filter(ville -> ville.getNbHabitants() >= min && ville.getNbHabitants() <= max)
                .toList();
    }
}
