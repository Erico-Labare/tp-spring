package fr.diginamic.hello.mapper;

import fr.diginamic.hello.dto.DepartementDto;
import fr.diginamic.hello.entities.Departement;
import fr.diginamic.hello.entities.Ville;
import org.springframework.stereotype.Component;

@Component
public class DepartementMapper {

    public DepartementDto toDto(Departement departement) {
        if (departement == null) {
            return null;
        }

        int totalHabitants = departement.getVilles() != null ? departement.getVilles().stream().mapToInt(Ville::getNbHabitants).sum() : 0;

        return new DepartementDto(departement.getCode(), departement.getNom(), totalHabitants);
    }

    public Departement toEntity(DepartementDto departementDto) {
        Departement departement = new Departement();
        departement.setCode(departementDto.getCodeDepartement());
        departement.setNom(departementDto.getNomDepartement());
        return departement;
    }
}
