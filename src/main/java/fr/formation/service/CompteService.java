package fr.formation.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.formation.model.Compte;
import fr.formation.repo.CompteRepository;

@Service
public class CompteService {

    @Autowired
    private CompteRepository compteRepository;

    public List<Compte> getAllComptes() {
        return compteRepository.findAll();
    }

    public Optional<Compte> getCompteById(String id) {
        return compteRepository.findById(id);
    }

    public Compte addCompte(Compte compte) {
        return compteRepository.save(compte);
    }

    public Compte updateCompte(String id, Compte newCompte) {
        if (compteRepository.existsById(id)) {
            newCompte.setId(id); // Assumption: id cannot be changed
            return compteRepository.save(newCompte);
        }
        return null; // Or throw an exception indicating the compte does not exist
    
    }
    public void deleteCompte(String id) {
        compteRepository.deleteById(id);
    }
}
