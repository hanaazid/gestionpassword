package fr.formation.api;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import fr.formation.model.Utilisateur;
import fr.formation.repo.CompteRepository;
import fr.formation.repo.UtilisateurRepository;
import fr.formation.request.ModifyUserRequest;
import fr.formation.service.NotesService;
import fr.formation.service.UtilisateurService;
import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;


	@RestController
	@RequestMapping("/api/utilisateurs")
	public class UtilisateurApiController {

	    @Autowired
	    private UtilisateurService utilisateurService;
	    
	    @Autowired
	    private CompteRepository compteRepo;
	    
	    @Autowired
	    private UtilisateurRepository userRepo;
	    
	    @Autowired
	    private NotesService notesService;

	    // Endpoint pour inscrire un utilisateur
	    @PostMapping("/inscription")
	    public ResponseEntity<?> inscrireUtilisateur(@RequestBody Utilisateur utilisateur) {
	        try {
	            utilisateurService.inscrireUtilisateur(utilisateur);
	            return ResponseEntity.ok("Inscription réussie");
	        } catch (IllegalArgumentException e) {
	            return ResponseEntity.badRequest().body(e.getMessage());
	        } catch (Exception e) {
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur interne du serveur");
	        }
	    }


	    // pour connecter un utilisateur
	    @PostMapping("/connexion")
	    public ResponseEntity<?> connecterUtilisateur(@RequestParam String email, @RequestParam String password) {
	        try {
	            Utilisateur utilisateur = utilisateurService.connecterUtilisateur(email, password);
	            return ResponseEntity.ok(utilisateur);
	        } catch (IllegalArgumentException e) {
	            return ResponseEntity.badRequest().body(e.getMessage());
	        }
	    }

	    // pour récupérer un mot de passe oublie
	    @PostMapping("/recuperation-mot-de-passe")
	    public ResponseEntity<?> recupererMotDePasseOublie(@RequestParam String email) {
	        try {
	            utilisateurService.recupererMotDePasseOublie(email);
	            return ResponseEntity.ok("Un email avec un mot de passe temporaire a été envoyé.");
	        } catch (IllegalArgumentException e) {
	            return ResponseEntity.badRequest().body(e.getMessage());
	        }
	    }
	    // List of Users
	   
	    // User modification 
	    @PutMapping  //("/{id}")
	    public void modifyUser(@Valid @RequestBody ModifyUserRequest request
	    		,@RequestParam("id") Integer id) {
	    	
	    	
	    	//Get the old value of the user
	    	Utilisateur oldUser =  userRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("Utilisateur non trouvé.")) ;
	    	
	    	
	    	if (oldUser.getMotPrimaire() != request.getMotPrimaire()) {
	    		//decrypt the old content and encrypt with the new motPrimaire
	    		
	    		notesService.modifyNotesByUserId(id,  oldUser.getMotPrimaire() , request.getMotPrimaire());
	    	};
	    	
	    	BeanUtils.copyProperties(request, oldUser); // update with new data
	    	//update the user
	    	userRepo.save(oldUser);
	    	
	    }
	        
	    // User Delete including it's account(Platform Web) and it's Notes  
	    @DeleteMapping("/{idUtilisateur}")
	    @Transactional
	    public void deleteUser(@PathVariable int idUtilisateur) {
	    	
	    	// delete it's account ( Platform Web Site
	    	compteRepo.deleteByUtilisateurId(idUtilisateur);
	    	
	    	// delete of it's note
	    	notesService.deleteNotesByUserId(idUtilisateur);
	    	
	    	// delete of the user
	    	utilisateurService.deleteUtilisateur(idUtilisateur);
	    	
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


	

