package fr.diginamic.hello.dto;

public class VilleDto {

    private int codeVille;
    private int nbHabitants;
    private String codeDepartement;
    private String nomDepartement;


    public int getCodeVille() {
        return codeVille;
    }

    public void setCodeVille(int codeVille) {
        this.codeVille = codeVille;
    }

    public int getNbHabitants() {
        return nbHabitants;
    }

    public void setNbHabitants(int nbHabitants) {
        this.nbHabitants = nbHabitants;
    }

    public String getCodeDepartement() {
        return codeDepartement;
    }

    public void setCodeDepartement(String codeDepartement) {
        this.codeDepartement = codeDepartement;
    }

    public String getNomDepartement() {
        return nomDepartement;
    }

    public void setNomDepartement(String nomDepartement) {
        this.nomDepartement = nomDepartement;
    }
}
