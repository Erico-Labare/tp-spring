package fr.diginamic.hello.repository;

import fr.diginamic.hello.entities.Ville;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VilleRepository extends JpaRepository<Ville, Integer> {

    Ville findByNom(String nom);

    List<Ville> findByNomStartingWith(String prefix);

    List<Ville> findByNbHabitantsGreaterThan(int min);

    List<Ville> findByNbHabitantsBetween(int min, int max);

    @Query("SELECT v FROM Ville v WHERE v.departement.id = :departementId AND v.nbHabitants > :min")
    List<Ville> findByDepartementAndPopulationGreaterThan(@Param("departementId") int departementId, @Param("min") int min);

    @Query("SELECT v FROM Ville v WHERE v.departement.id = :departementId AND v.nbHabitants BETWEEN :min AND :max")
    List<Ville> findByDepartementAndPopulationBetween(@Param("departementId") int departementId, @Param("min") int min, @Param("max") int max);

    @Query("SELECT v FROM Ville v WHERE v.departement.id = :departementId ORDER BY v.nbHabitants DESC")
    List<Ville> findTopNVillesByDepartement(@Param("departementId") int departementId, Pageable pageable);
}
