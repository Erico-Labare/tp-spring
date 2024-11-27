package fr.diginamic.hello.repository;

import fr.diginamic.hello.entities.Departement;
import fr.diginamic.hello.entities.Ville;
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

    @Query("SELECT v FROM Ville v WHERE v.departement.id = :departementId ORDER BY v.nbHabitants DESC")
    List<Ville> findTopNVillesByDepartement(@Param("departementId") int departementId, @Param("n") int n);

    @Query("SELECT v FROM Ville v WHERE v.departement.id = :departementId AND v.nbHabitants BETWEEN :min AND :max")
    List<Ville> findVillesByPopulationRangeAndDepartement(@Param("departementId") int departementId, @Param("min") int min,@Param("max") int max);
}
