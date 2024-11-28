package fr.diginamic.hello.services;

import fr.diginamic.hello.dto.DepartementDto;
import fr.diginamic.hello.entities.Departement;
import fr.diginamic.hello.entities.Ville;
import fr.diginamic.hello.exception.FunctionalException;
import fr.diginamic.hello.repository.DepartementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class DepartementService {

    @Autowired
    private DepartementRepository departementRepository;

    @Transactional
    public List<Departement> extractAllDepartements() {
        return departementRepository.findAllDepartements();
    }

    @Transactional
    public Departement extractDepartementParId(int id) {
        return departementRepository.findById(id).orElse(null);
    }

    @Transactional
    public Departement extractDepartementParNom(String nom) {
        return departementRepository.findByNom(nom).orElse(null);
    }

    @Transactional
    public void insertDepartement(Departement departement) {
        departementRepository.save(departement);
    }

    @Transactional
    public void modifierDepartement(int id, Departement departementModifie) {
        Departement departement = departementRepository.findById(id).orElse(null);
        if (departement != null) {
            departement.setCode(departementModifie.getCode());
            departement.setNom(departementModifie.getNom());
            departementRepository.save(departement);
        }
    }

    @Transactional
    public void supprimerDepartement(int id) {
        departementRepository.deleteById(id);
    }

    @Transactional
    public List<Ville> extractTopNVillesParDepartement(int departementId, int n) {
        Pageable pageable = PageRequest.of(0, n);
        return departementRepository.findTopNVillesByDepartement(departementId, pageable);
    }

    @Transactional
    public List<Ville> extractVillesEntreParDepartement(int departementId, int min, int max) {
        return departementRepository.findVillesByPopulationRangeAndDepartement(departementId, min, max);
    }

    public void validateDepartement(DepartementDto departementDto) throws FunctionalException {
        if (departementDto.getCodeDepartement() == null || departementDto.getCodeDepartement().length() < 2 || departementDto.getCodeDepartement().length() > 3) {
            throw new FunctionalException("Le code département doit contenir entre 2 et 3 caractères.");
        }
        if (departementDto.getNomDepartement() == null || departementDto.getNomDepartement().length() < 3) {
            throw new FunctionalException("Le nom du département est obligatoire et doit contenir au moins 3 lettres.");
        }
        Optional<Departement> existingDepartement = departementRepository.findByCode(departementDto.getCodeDepartement());
        if (existingDepartement.isPresent()) {
            throw new FunctionalException("Un département avec ce code existe déjà.");
        }
    }
}
