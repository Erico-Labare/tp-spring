package fr.diginamic.hello.mapper;

import fr.diginamic.hello.dto.VilleDto;
import fr.diginamic.hello.entities.Departement;
import fr.diginamic.hello.entities.Ville;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class VilleMapper {

    @Autowired
    private DepartementMapper departementMapper;

    public VilleDto toDto(Ville ville) {
        VilleDto villeDto = new VilleDto();
        villeDto.setCodeVille(ville.getId());
        villeDto.setNbHabitants(ville.getNbHabitants());
        if (ville.getDepartement() != null) {
            villeDto.setCodeDepartement(ville.getDepartement().getCode());
            villeDto.setNomDepartement(ville.getDepartement().getNom());
        }
        return villeDto;
    }

    public Ville toEntity(VilleDto villeDto, Departement departement) {
        Ville ville = new Ville();
        ville.setId(villeDto.getCodeVille());
        ville.setNbHabitants(villeDto.getNbHabitants());
        ville.setDepartement(departement);
        return ville;
    }
}