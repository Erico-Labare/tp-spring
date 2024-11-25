package fr.diginamic.hello.services;

import fr.diginamic.hello.dao.VilleDao;
import fr.diginamic.hello.entities.Ville;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VilleService {

    @Autowired
    private VilleDao villeDao;

    public List<Ville> extractAllVilles() {
        return villeDao.extractVilles();
    }

    public Ville extractVilleParId(int idVille) {
        return villeDao.extractVilleById(idVille);
    }

    public Ville extractVilleParNom(String nom) {
        return villeDao.extractVilleByName(nom);
    }

    public List<Ville> insertVille(Ville ville) {
        villeDao.insertVille(ville);
        return extractAllVilles();
    }

    public List<Ville> modifierVille(int idVille, Ville villeModifiee) {
        Ville ville = villeDao.extractVilleById(idVille);
        if (ville != null) {
            ville.setNom(villeModifiee.getNom());
            ville.setNbHabitants(villeModifiee.getNbHabitants());
            villeDao.updateVille(ville);
        }
        return extractAllVilles();
    }

    public List<Ville> supprimerVille(int idVille) {
        villeDao.deleteVille(idVille);
        return extractAllVilles();
    }
}