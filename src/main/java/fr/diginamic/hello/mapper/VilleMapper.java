package fr.diginamic.hello.mapper;

import fr.diginamic.hello.dto.VilleDto;
import fr.diginamic.hello.entities.Departement;
import fr.diginamic.hello.entities.Ville;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class VilleMapper {

    public VilleDto toDto(Ville ville) {
        if (ville == null || ville.getDepartement() == null) {
            return null;
        }
        return new VilleDto(ville.getId(), ville.getNbHabitants(), ville.getDepartement() != null ? ville.getDepartement().getCode() : null, ville.getDepartement() != null ? ville.getDepartement().getNom() : null);
    }

    public Ville toEntity(VilleDto villeDto) {
        Ville ville = new Ville();
        ville.setId(villeDto.getCodeVille());

        ville.setNbHabitants(villeDto.getNbHabitants());
        if (villeDto.getCodeDepartement() != null) {
            Departement departement = new Departement();
            departement.setCode(villeDto.getCodeDepartement());
            departement.setNom(villeDto.getNomDepartement());
            ville.setDepartement(departement);
        }

        return ville;
    }
}