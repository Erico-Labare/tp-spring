package fr.diginamic.hello.mapper;

import fr.diginamic.hello.dto.DepartementDto;
import fr.diginamic.hello.entities.Departement;
import fr.diginamic.hello.entities.Ville;
import org.springframework.stereotype.Component;

@Component
public class DepartementMapper {

    public DepartementDto toDto(Departement departement) {
        DepartementDto departementDto = new DepartementDto();
        departementDto.setCodeDepartement(departement.getCode());
        departementDto.setNomDepartement(departement.getNom());
        int totalHabitants = departement.getVilles().stream().mapToInt(Ville::getNbHabitants).sum();
        departementDto.setNbHabitants(totalHabitants);
        return departementDto;
    }

    public Departement toEntity(DepartementDto departementDto) {
        Departement departement = new Departement();
        departement.setCode(departementDto.getCodeDepartement());
        departement.setNom(departementDto.getNomDepartement());
        return departement;
    }
}
