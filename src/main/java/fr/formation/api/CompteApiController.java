package fr.formation.api;

import java.time.LocalDateTime;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import fr.formation.model.Compte;
import fr.formation.model.Utilisateur;
import fr.formation.repo.UtilisateurRepository;
import fr.formation.request.ComptesUserRequest;
import fr.formation.request.CreateCompteRequest;
import fr.formation.request.ModifyCompteRequest;
import fr.formation.response.EntityCreatedResponse;
import fr.formation.service.CompteService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/compte")
@CrossOrigin("*")
public class CompteApiController {

    @Autowired
    private CompteService compteService;
    

    @Autowired
    private UtilisateurRepository userRepo;

    Logger logger = LoggerFactory.getLogger(CompteApiController.class);
    

    @GetMapping
    public ResponseEntity<List<Compte>> getAllComptes() {
        List<Compte> comptes = compteService.getAllComptes();
        return ResponseEntity.ok(comptes);
    }

    @GetMapping("/compte")
    public ResponseEntity<Compte> getCompteById(@RequestParam Integer id) {
        Compte compte = compteService.getCompteById(id);
        return ResponseEntity.ok(compte);
    }
    @GetMapping("/utilisateur") //findByUtilisateurId
    public ResponseEntity<List<Compte>> getAllComptesByUser(@RequestParam Integer idUtilisateur) {
        List<Compte> comptes = compteService.findByUtilisateurId(idUtilisateur);
        return ResponseEntity.ok(comptes);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EntityCreatedResponse addCompte(@Valid @RequestBody CreateCompteRequest request) {
        Integer newCompteId = this.compteService.addCompte(request);
        logger.debug("Compte {} created!", newCompteId);
        return new EntityCreatedResponse(newCompteId, request.getUtilisateurId());
    }

    @PutMapping  //("/{id}")
    public ResponseEntity<Compte> updateCompte(@Valid @RequestBody ModifyCompteRequest request, 
    		@RequestParam("id") Integer id) {
        if (!compteService.existsCompteById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "ID Compte inexistant");
        }

        Compte existingCompte = compteService.getCompteById(id);
        BeanUtils.copyProperties(request, existingCompte, "id", "dateAdded", "dateUpdate");

        existingCompte.setDateUpdate(LocalDateTime.now());
        
        Utilisateur user = userRepo.getById(existingCompte.getUtilisateur().getId());
        System.out.println("Avant update *******");
        
        
        
        Compte savedCompte = compteService.updateCompte(existingCompte, user.getMotPrimaire() );

        return ResponseEntity.ok(savedCompte);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteCompte(@RequestParam Integer id) {
        compteService.deleteCompte(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/utilisateurs/{idUtilisateur}")
    public List<Compte> getComptesByIdUtilisateur(@PathVariable Integer idUtilisateur) {
        return compteService.findByUtilisateurId(idUtilisateur);
    }

}
