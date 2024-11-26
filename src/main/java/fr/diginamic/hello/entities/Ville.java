package fr.diginamic.hello.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "ville")
public class Ville {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private int id;

    @NotBlank
    @Size(min = 2)
    @Column(name = "NOM")
    private String nom;

    @Column(name = "ID_REGION")
    private String id_region;

    @Min(value = 1)
    @Column(name = "NB_HABS")
    private int nbHabitants;

    @ManyToOne
    @JoinColumn(name = "ID_DEPT")
    @JsonBackReference
    private Departement departement;

    public Ville() {
    }

    public Ville(int id, String nom, String id_region, int nbHabitants, Departement departement) {
        this.id = id;
        this.nom = nom;
        this.id_region = id_region;
        this.nbHabitants = nbHabitants;
        this.departement = departement;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public int getNbHabitants() {
        return nbHabitants;
    }

    public void setNbHabitants(int nbHabitants) {
        this.nbHabitants = nbHabitants;
    }

    public Departement getDepartement() {
        return departement;
    }

    public void setDepartement(Departement departement) {
        this.departement = departement;
    }


}

