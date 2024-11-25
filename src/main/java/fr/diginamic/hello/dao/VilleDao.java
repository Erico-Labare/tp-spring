package fr.diginamic.hello.dao;

import fr.diginamic.hello.entities.Ville;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public class VilleDao {

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public List<Ville> extractVilles() {
        return em.createQuery("SELECT v FROM Ville v", Ville.class).getResultList();
    }

    @Transactional
    public Ville extractVilleById(int idVille) {
        return em.find(Ville.class, idVille);
    }

    @Transactional
    public Ville extractVilleByName(String nom) {
        try {
            return em.createQuery("SELECT v FROM Ville v WHERE v.nom = :nom", Ville.class).setParameter("nom", nom).getSingleResult();
        } catch (jakarta.persistence.NoResultException e) {
            return null;
        }
    }

    @Transactional
    public void insertVille(Ville ville) {
        em.persist(ville);
    }

    @Transactional
    public Ville updateVille(Ville ville) {
        return em.merge(ville);
    }

    @Transactional
    public void deleteVille(int idVille) {
        Ville ville = extractVilleById(idVille);
        if (ville != null) {
            em.remove(ville);
        }
    }


}
