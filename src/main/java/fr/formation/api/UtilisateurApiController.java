package fr.formation.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import fr.formation.model.Utilisateur;
import fr.formation.service.UtilisateurService;

public class UtilisateurApiController {
	@RestController
	@RequestMapping("/api/utilisateurs")
	public class UtilisateurController {

	    @Autowired
	    private UtilisateurService utilisateurService;

	    // Endpoint pour inscrire un utilisateur
	    @PostMapping("/inscription")
	    public ResponseEntity<?> inscrireUtilisateur(@RequestBody Utilisateur utilisateur) {
	        try {
	            utilisateurService.inscrireUtilisateur(utilisateur);
	            return ResponseEntity.ok("Inscription réussie");
	        } catch (IllegalArgumentException e) {
	            return ResponseEntity.badRequest().body(e.getMessage());
	        }
	    }

	    // Endpoint pour connecter un utilisateur
	    @PostMapping("/connexion")
	    public ResponseEntity<?> connecterUtilisateur(@RequestParam String email, @RequestParam String motDePasse) {
	        try {
	            Utilisateur utilisateur = utilisateurService.connecterUtilisateur(email, motDePasse);
	            return ResponseEntity.ok(utilisateur);
	        } catch (IllegalArgumentException e) {
	            return ResponseEntity.badRequest().body(e.getMessage());
	        }
	    }

	    // Endpoint pour récupérer un mot de passe oublie
	    @PostMapping("/recuperation-mot-de-passe")
	    public ResponseEntity<?> recupererMotDePasseOublie(@RequestParam String email) {
	        try {
	            utilisateurService.recupererMotDePasseOublie(email);
	            return ResponseEntity.ok("Un email avec un mot de passe temporaire a été envoyé.");
	        } catch (IllegalArgumentException e) {
	            return ResponseEntity.badRequest().body(e.getMessage());
	        }
	    }
	}
    // Endpoint pour récupérer la liste des utilisateurs avec les mots de passe masqués
   // @GetMapping("/liste-mots-de-passe-masques")
	/*
	 * public Iterable<Utilisateur> getListeUtilisateursMasques() { return
	 * utilisateurService.getListeUtilisateursMasques(); }
	 * 
	 * // Endpoint pour récupérer un utilisateur avec son mot de passe visible après
	 * "consulter"
	 * 
	 * @GetMapping("/{id}") public Utilisateur getUtilisateur(@PathVariable Long id)
	 * { return utilisateurService.getUtilisateur(id); }
	 */
}

	

