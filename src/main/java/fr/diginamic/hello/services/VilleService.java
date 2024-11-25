package fr.diginamic.hello.services;

import fr.diginamic.hello.entities.Ville;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class VilleService {

    private List<Ville> villes = new ArrayList<>();

    public VilleService() {
        villes.add(new Ville(1, "Paris", 2148000));
        villes.add(new Ville(2, "Lyon", 513000));
        villes.add(new Ville(3, "Marseille", 861000));
        villes.add(new Ville(4, "Toulouse", 479000));
    }

    public List<Ville> getListVille() {
        return villes;
    }

    public Ville getVilleById(int id) {
        return villes.stream().filter(v -> v.getId() == id).findFirst().orElse(null);
    }

    public boolean ajouterVille(Ville ville) {
        if (villes.stream().anyMatch(v -> v.getId() == ville.getId())) {
            return false;
        }
        villes.add(ville);
        return true;
    }

    public boolean modifierVille(int id, Ville villeModifiee) {
        Ville ville = getVilleById(id);
        if (ville != null) {
            ville.setNom(villeModifiee.getNom());
            ville.setNbHabitants(villeModifiee.getNbHabitants());
            return true;
        }
        return false;
    }

    public boolean supprimerVille(int id) {
        Ville ville = getVilleById(id);
        if (ville != null) {
            villes.remove(ville);
            return true;
        }
        return false;
    }
}