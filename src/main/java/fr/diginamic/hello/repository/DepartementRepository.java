package fr.diginamic.hello.repository;

import fr.diginamic.hello.entities.Departement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DepartementRepository extends JpaRepository<Departement, Integer> {

    Optional<Departement> findByNom(String nom);

    @Query("SELECT d FROM Departement d")
    List<Departement> findAllDepartements();

    @Query("SELECT SUM(v.nbHabitants) FROM Ville v WHERE v.departement.id = :departementId")
    int findTotalHabitantsByDepartementId(@Param("departementId") int departementId);
}
