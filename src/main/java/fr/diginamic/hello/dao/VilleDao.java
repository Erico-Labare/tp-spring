package fr.diginamic.hello.dao;

import fr.diginamic.hello.entities.Ville;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public class VilleDao {

    @PersistenceContext
    private EntityManager entityManager;

    public List<Ville> extractVilles() {
        return entityManager.createQuery("SELECT v FROM Ville v", Ville.class).getResultList();
    }

    public Ville extractVilleById(int idVille) {
        return entityManager.find(Ville.class, idVille);
    }

    public Ville extractVilleByName(String nom) {
        return entityManager.createQuery("SELECT v FROM Ville v WHERE v.nom = :nom", Ville.class)
                .setParameter("nom", nom)
                .getSingleResult();
    }

    public void insertVille(Ville ville) {
        entityManager.persist(ville);
    }

    public Ville updateVille(Ville ville) {
        return entityManager.merge(ville);
    }

    public void deleteVille(int idVille) {
        Ville ville = extractVilleById(idVille);
        if (ville != null) {
            entityManager.remove(ville);
        }
    }


}
