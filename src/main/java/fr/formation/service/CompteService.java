package fr.formation.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import fr.formation.model.Compte;
import fr.formation.model.Utilisateur;
import fr.formation.repo.CompteRepository;
import fr.formation.repo.UtilisateurRepository;
import fr.formation.request.CreateCompteRequest;

@Service
public class CompteService {

    @Autowired
    private CompteRepository compteRepository;

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    public List<Compte> getAllComptes() {
        return compteRepository.findAll();
    }

    public Compte getCompteById(Integer id) {
        return compteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Compte non trouvé avec ID: " + id));
    }

    public Integer addCompte(CreateCompteRequest compteRequest) {
        Utilisateur utilisateur = utilisateurRepository.getById(compteRequest.getUtilisateurId());
        Compte compte = new Compte();
        BeanUtils.copyProperties(compteRequest, compte);
        compte.setDateAdded(LocalDateTime.now());
        compte.setDateUpdate(LocalDateTime.now());
        compte.setUtilisateur(utilisateur);
        compteRepository.save(compte);
        return compte.getId();
    }

    public Compte updateCompte(Compte compte) {
        if (compte.getId() == null) {
            throw new RuntimeException("Besoin d'un id pour faire une mise à jour.");
        }

        Compte existingCompte = getCompteById(compte.getId());

        // Copie les propriétés mises à jour, mais conserve certaines anciennes propriétés
        existingCompte.setPlatformname(compte.getPlatformname());
        existingCompte.setDescription(compte.getDescription());
        existingCompte.setUserName(compte.getUserName());
        existingCompte.setEmail(compte.getEmail());
        existingCompte.setAdressUrl(compte.getAdressUrl());
        existingCompte.setPassword(compte.getPassword());
        existingCompte.setDateUpdate(LocalDateTime.now());

        return compteRepository.save(existingCompte);
    }

    public boolean existsCompteById(Integer id) {
        return compteRepository.existsById(id);
    }

    public void deleteCompte(Integer id) {
        if (compteRepository.existsById(id)) {
            compteRepository.deleteById(id);
        }
    }

    public List<Compte> findByUtilisateurId(Integer idUtilisateur) {
        return compteRepository.findByUtilisateurId(idUtilisateur);
    }
}
