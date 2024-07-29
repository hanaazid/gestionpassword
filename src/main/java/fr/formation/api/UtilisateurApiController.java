package fr.formation.api;

import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;

import javax.crypto.NoSuchPaddingException;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import fr.formation.model.Compte;
import fr.formation.model.Utilisateur;
import fr.formation.repo.CompteRepository;
import fr.formation.repo.UtilisateurRepository;
import fr.formation.request.CreateUserRequest;
import fr.formation.request.LoginRequest;
import fr.formation.request.ModifyUserRequest;
import fr.formation.service.NotesService;
import fr.formation.service.PasswordConvertService;
import fr.formation.service.PasswordUtils;
import fr.formation.service.UtilisateurService;
import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;


	@RestController
	@RequestMapping("/api/utilisateurs")
	@CrossOrigin("*")
	public class UtilisateurApiController {

	    @Autowired
	    private UtilisateurService utilisateurService;
	    
	    @Autowired
	    private PasswordUtils pwdHashedService;
	    @Autowired
	    private CompteRepository compteRepo;
	    
	    @Autowired
	    private UtilisateurRepository userRepo;
	    
	    @Autowired
	    private NotesService notesService;
	    
	    @Autowired
	    private PasswordConvertService pwdService;
	   
	    
	    @GetMapping("/email")
	    public ResponseEntity<Optional<Utilisateur>> getUserByEmail(@RequestParam String email) {
	    	
	        Optional<Utilisateur> user = userRepo.findByEmail(email);
	        return ResponseEntity.ok(user);
	    }
	    @GetMapping("")
	    public ResponseEntity<Optional<Utilisateur>> getUserById(@RequestParam Integer id) {
	        Optional<Utilisateur> user = userRepo.findById(id);
	        return ResponseEntity.ok(user);
	    }
	    @GetMapping("/login")
	    public ResponseEntity<?> login(@RequestParam String email, @RequestParam String password ) {
	        	        
	    	String pwdHashed = pwdHashedService.hashPassword(password);
	    	System.out.println("Pwd Hashed :in login password : " + password );
	    	System.out.println("Pwd Hashed :in login password : " + pwdHashed );
	    	
	        Optional<Utilisateur> user = userRepo.findByEmail(email) ;
	        
	        Boolean bool = pwdHashedService.checkPassword(password, user.get().getPassword());
	    	//String encryptPassword = "";
	    	
	    	
			//encryptPassword = pwdService.encrypt_Sha_1(request.getPassword());
				
	    	//if (encryptPassword == user.getPassword()) {
	        if ( !user.isPresent() || !bool ) {
	        	System.out.println("KO - email");
	    		return ResponseEntity.badRequest().body("Adresse e-mail / password incorrect.");
	    		
	        }
	        else {
	    		System.out.println("ok - password");
	    		 return ResponseEntity.ok(user);	
	        }
	    }

	    // Endpoint pour inscrire un utilisateur
	    @PostMapping("/inscription")
//	    public ResponseEntity<?> inscrireUtilisateur(@RequestBody Utilisateur utilisateur) {
	    public ResponseEntity<?> inscrireUtilisateur(@RequestBody CreateUserRequest utilisateurRequest) {
	        try {
	        	Utilisateur user =  
	        		utilisateurService.inscrireUtilisateur(utilisateurRequest);
	        	if ( user == null  ) {
	        		return ResponseEntity.badRequest().body("Anomalie création de user");
	        	}
	        	else {
	            return ResponseEntity.ok(user);
	        	}
	        } catch (IllegalArgumentException e) {
	            return ResponseEntity.badRequest().body(e.getMessage());
	        } catch (Exception e) {
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur interne du serveur");
	        }
	    }


	    // pour connecter un utilisateur
	   // @PostMapping("/connexion")
	   // public ResponseEntity<?> connecterUtilisateur(@RequestParam String email, @RequestParam String password) {
	    		    	
	    	/* Optional<Utilisateur> user = 			userRepo.findByEmail(email) ;
	    	String encryptPassword = "";
	    	
	    	
			encryptPassword = pwdService.encrypt_Sha_1(password); 
			
	    	System.out.println("connexion : " + email + " : " + password);
	    	System.out.println("password crypté"+ encryptPassword);
	    	
	    	if (encryptPassword == user.getPassword()) {
	    		 return ResponseEntity.ok(user);
	    	}else {
	    		return ResponseEntity.badRequest().body("Adresse e-mail ou mot de passe incorrect.");
	    		//throw new IllegalArgumentException("Adresse e-mail ou mot de passe incorrect.");
	    	}
	    	*/
	        /*try {
	        	
	        	
	            Utilisateur utilisateur = utilisateurService.connecterUtilisateur(email, password);
	            return ResponseEntity.ok(utilisateur);
	        } catch (IllegalArgumentException e) {
	            return ResponseEntity.badRequest().body(e.getMessage());
	        }*/
	    //}

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
	    @PutMapping   
	    public void modifyUser(@Valid @RequestBody ModifyUserRequest request
	    		,@RequestParam("id") Integer id) {
	    	
	    	
	    	//Get the old value of the user
	    	Utilisateur oldUser =  userRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("Utilisateur non trouvé.")) ;
	    	
	    	
	    	if (oldUser.getMotPrimaire() != request.getMotPrimaire()) {
	    		//decrypt the old content and encrypt with the new motPrimaire
	    		
	    		List<Compte> lcompte =   compteRepo.findByUtilisateurId(id);
	    		String motPrimaireOld = oldUser.getMotPrimaire();
	    		String motPrimaire = request.getMotPrimaire();
	    		String pwdTmp = "";
	    		for (Compte compte : lcompte) {
	    			pwdTmp = compte.getPassword();
	    			try {
	    				pwdTmp = pwdService.decryptWithString(pwdTmp, motPrimaireOld);
	    			} catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
	    				// TODO Auto-generated catch block
	    				e.printStackTrace();
	    			}
	    			pwdTmp = pwdService.removeEvenIndexedChars(pwdTmp);
	    			
	    			pwdTmp = pwdService.insertRandomDigits(pwdTmp);
	    			try {
	    				
	    				pwdTmp = pwdService.encryptWithString(pwdTmp, motPrimaireOld);
	    			} catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
	    				// TODO Auto-generated catch block
	    				e.printStackTrace();
	    			}
	    			
	        		compte.setPassword(pwdTmp);
	        		compteRepo.save(compte);
	        	
	    		
	            }
	    		
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
    

	

