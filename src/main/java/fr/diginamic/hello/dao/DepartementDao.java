package fr.diginamic.hello.dao;

import fr.diginamic.hello.entities.Departement;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public class DepartementDao {

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public List<Departement> extractDepartements() {
        return em.createQuery("SELECT d FROM Departement d", Departement.class).getResultList();
    }

    @Transactional
    public Departement extractDepartementById(int id) {
        return em.find(Departement.class, id);
    }

    @Transactional
    public Departement extractDepartementByName(String nom) {
        try {
            return em.createQuery("SELECT d FROM Departement d WHERE d.nom = :nom", Departement.class).setParameter("nom", nom).getSingleResult();
        } catch (jakarta.persistence.NoResultException e) {
            return null;
        }
    }

    @Transactional
    public void insertDepartement(Departement departement) {
        em.persist(departement);
    }

    @Transactional
    public Departement updateDepartement(Departement departement) {
        return em.merge(departement);
    }

    @Transactional
    public void deleteDepartement(int id) {
        Departement departement = extractDepartementById(id);
        if (departement != null) {
            em.remove(departement);
        }
    }
}