package fr.diginamic.hello.services;

import fr.diginamic.hello.dao.VilleDao;
import fr.diginamic.hello.entities.Ville;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class VilleService {

    @Autowired
    private VilleDao villeDao;

    public List<Ville> extractVilles() {
        return villeDao.extractVilles();
    }

    public Ville extractVille(int idVille) {
        return villeDao.extractVilleById(idVille);
    }

    public Ville extractVille(String nom) {
        return villeDao.extractVilleByName(nom);
    }

    public List<Ville> insertVille(Ville ville) {
        villeDao.insertVille(ville);
        return extractVilles();
    }

    public List<Ville> modifierVille(int idVille, Ville villeModifiee) {
        Ville ville = villeDao.extractVilleById(idVille);
        if (ville != null) {
            ville.setNom(villeModifiee.getNom());
            ville.setNbHabitants(villeModifiee.getNbHabitants());
            villeDao.updateVille(ville);
        }
        return extractVilles();
    }

    public List<Ville> supprimerVille(int idVille) {
        villeDao.deleteVille(idVille);
        return extractVilles();
    }
}