package fr.formation.api;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.formation.model.Compte;
import fr.formation.repo.CompteRepository;
import fr.formation.service.CompteService;


@RestController
@RequestMapping("/api/compte")
public class CompteApiController {
	
	@Autowired
	
	 private CompteService compteService;

    @GetMapping
    public ResponseEntity<List<Compte>> getAllComptes() {
        List<Compte> comptes = compteService.getAllComptes();
        return ResponseEntity.ok(comptes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Compte> getCompteById(@PathVariable String id) {
        Optional<Compte> compte = compteService.getCompteById(id);
        return compte.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Compte> addCompte(@RequestBody Compte compte) {
        Compte newCompte = compteService.addCompte(compte);
        return ResponseEntity.status(HttpStatus.CREATED).body(newCompte);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Compte> updateCompte(@PathVariable String id, @RequestBody Compte newCompte) {
        Compte updatedCompte = compteService.updateCompte(id, newCompte);
        if (updatedCompte != null) {
            return ResponseEntity.ok(updatedCompte);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCompte(@PathVariable String id) {
        compteService.deleteCompte(id);
        return ResponseEntity.noContent().build();
    }
}
	

	
	


