package fr.diginamic.hello.services;

import fr.diginamic.hello.dto.VilleDto;
import fr.diginamic.hello.entities.Departement;
import fr.diginamic.hello.entities.Ville;
import fr.diginamic.hello.exception.FunctionalException;
import fr.diginamic.hello.repository.DepartementRepository;
import fr.diginamic.hello.repository.VilleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class VilleService {

    @Autowired
    private VilleRepository villeRepository;

    @Autowired
    private DepartementRepository departementRepository;

    @Transactional
    public List<Ville> extractAllVilles() {
        return villeRepository.findAll();
    }

    @Transactional
    public Page<Ville> extractAllVillesPage(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return villeRepository.findAll(pageable);
    }

    @Transactional
    public Ville extractVilleParId(int idVille) {
        return villeRepository.findById(idVille).orElse(null);
    }

    @Transactional
    public Ville extractVilleParNom(String nom) {
        return villeRepository.findByNom(nom);
    }

    @Transactional
    public void insertVille(Ville ville) {
        Optional<Departement> optionalDepartement = departementRepository.findByCode(ville.getDepartement().getCode());
        if (optionalDepartement.isPresent()) {
            ville.setDepartement(optionalDepartement.get());
            villeRepository.save(ville);
        }

    }

    @Transactional
    public void modifierVille(int idVille, Ville villeModifiee) {
        Ville villeExistante = villeRepository.findById(idVille).orElse(null);
        if (villeExistante != null) {
            villeExistante.setNom(villeModifiee.getNom());
            villeExistante.setNbHabitants(villeModifiee.getNbHabitants());

            Optional<Departement> optionalDepartement = departementRepository.findById(villeModifiee.getDepartement().getId());
            if (optionalDepartement.isPresent()) {
                villeExistante.setDepartement(optionalDepartement.get());
                villeRepository.save(villeExistante);
            }

        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public void supprimerVille(int idVille) {
        villeRepository.deleteById(idVille);
    }

    @Transactional
    public List<Ville> extractVillesParPrefixe(String prefix) {
        return villeRepository.findByNomStartingWith(prefix);
    }

    @Transactional
    public List<Ville> extractVillesPopulationSup1(int min) {
        return villeRepository.findByNbHabitantsGreaterThan(min);
    }

    @Transactional
    public List<Ville> extractVillesPopulationEntre(int min, int max) {
        return villeRepository.findByNbHabitantsBetween(min, max);
    }

    @Transactional
    public List<Ville> extractVillesDepartementPopulationSupA(int departementId, int min) {
        return villeRepository.findByDepartementIdAndNbHabitantsGreaterThan(departementId, min);
    }

    @Transactional
    public List<Ville> extractVillesDepartementPopulationEntre(int departementId, int min, int max) {
        return villeRepository.findByDepartementIdAndNbHabitantsBetween(departementId, min, max);
    }

    @Transactional
    public List<Ville> extractTopNVillesDepartement(int departementId, int n) {
        Pageable pageable = PageRequest.of(0, n);
        return villeRepository.findTopNByDepartementIdOrderByNbHabitantsDesc(departementId, pageable);
    }

    public void validateVille(VilleDto villeDto) throws FunctionalException {
        if (villeDto.getNbHabitants() < 10) {
            throw new FunctionalException("La ville doit avoir au moins 10 habitants.");
        }
        if (villeDto.getNomVille() == null || villeDto.getNomVille().length() < 2) {
            throw new FunctionalException("Le nom de la ville doit contenir au moins 2 lettres.");
        }
        if (villeDto.getCodeDepartement() == null || villeDto.getCodeDepartement().length() != 2) {
            throw new FunctionalException("Le code département doit contenir exactement 2 caractères.");
        }
    }
}