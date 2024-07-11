package fr.formation.apinotes;

import java.util.List;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.NoSuchPaddingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
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
import org.springframework.web.client.RestTemplate;

import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;



import fr.formation.notes.CreateNotesRequest;
import fr.formation.notes.ModifyNotesRequest;
import fr.formation.notes.Notes;
import fr.formation.repo.UtilisateurRepository;
import fr.formation.response.EntityCreatedResponse;
import fr.formation.service.PasswordConvertService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/user/notes")
@CrossOrigin("*")
public class NotesApiController {

	@Autowired
	public RestTemplate restTemplate; 
	
	@Autowired
	public PasswordConvertService cryptService = new PasswordConvertService();
	
	@Autowired
	public UtilisateurRepository repoUser;
	private static final Logger logger = LoggerFactory.getLogger(NotesApiController.class);
	
	

	@GetMapping("")
	public List<Notes> getNoteByIdUtilsateur(@RequestParam int idUtilisateur)
	{
		// The note must be returned with the content clear ( decrypted )
		String requestUrl = "http://localhost:8081/api/notes/" + idUtilisateur+ "/allnotesuser";
		
        ResponseEntity<List<Notes>> response = this.restTemplate
                .exchange(requestUrl,   
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<List<Notes>>() {});
        List<Notes> notes = response.getBody() ;
        
        List<Notes> notesClear = new ArrayList<Notes>();
        String contenuClear = "";
        Integer idUser = idUtilisateur;
        
        for ( Notes note : notes ) {
        	contenuClear = note.getContenu();
        	
        	try {
				contenuClear = this.cryptService.decrypt( note.getContenu(), idUser);
				System.out.println(contenuClear);
			} catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
				// TODO Auto-generated catch block
				logger.error("Encrypt/Decrypt bug ");
				e.printStackTrace();
			}
        	note.setContenu(contenuClear);
        	notesClear.add(note);
        	 
        }
        
		return notesClear ; 
	}
	
	@GetMapping("/{idNote}/note")
	public Boolean getExistNoteByIdNote(@PathVariable int idNote)
	{
		String requestUrl = "http://localhost:8081/api/notes/" + idNote+"/note";
        
		Boolean bool = Boolean.FALSE;
        bool = this.restTemplate.getForObject(requestUrl, Boolean.class);
        
        if (!bool ) { logger.debug("Note {} not exist!", idNote);};
        return bool;

	}
	// Création de note
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EntityCreatedResponse create(@Valid @RequestBody CreateNotesRequest request) {
    	
		EntityCreatedResponse entity = new EntityCreatedResponse();
		// Url address
		String requestUrl = "http://localhost:8081/api/notes";
		 
		 //Verify the user exist in table Utilisateur
		Boolean userExist = repoUser.existsById(request.getIdUtilisateur());
		if ( !userExist ) {
			 logger.debug("User {} not exist!", request.getIdUtilisateur());
			 return entity;
		 };
		 // Encrypt the content of the note
		String contextACrypter = "";
		try {
			contextACrypter = cryptService.encrypt(request.getContenu(), request.getIdUtilisateur());
		} catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
			// TODO Auto-generated catch block
				e.printStackTrace();
				
				return entity;
		}
		 request.setContenu(contextACrypter);
		    		 
		 ResponseEntity<Notes> response = this.restTemplate
		        .postForEntity( requestUrl, request , Notes.class);
		 
		
		 logger.debug("Note {} created!", response.getBody().getId());
		
		 return new EntityCreatedResponse(response.getBody().getId(), request.getIdUtilisateur());

  }
  // Màj de la note
    @PutMapping //("/{id}")
    public EntityCreatedResponse modify(@Valid @RequestBody ModifyNotesRequest request,
    		@RequestParam("id") Integer id) {
        
    	EntityCreatedResponse entity = new EntityCreatedResponse();
    	
    	if (request.getId() != id) {
    		 logger.error("Data are not correct : {} / {]", request.getId());
    		 return entity;
    	}
    	
       //Verify the user exist in table Utilisateur  
        Boolean userExist = repoUser.existsById(request.getIdUtilisateur());
        if ( !userExist ) {
       	 	
       	 logger.error("User {} not exist!", request.getIdUtilisateur());
       	 	return entity;
        };
        //Verify that the note exist in the remote table
        Boolean noteExist =  Boolean.FALSE;
        
        String requestUrlNote = "http://localhost:8081/api/notes/" + request.getId()+"/note";
        noteExist = this.restTemplate.getForObject(requestUrlNote, Boolean.class);
        if(!noteExist){
          	// EntityCreatedResponse entity = new EntityCreatedResponse();
          	logger.error("Note {} not exist!", request.getId());
           	 return entity;
         };
        
        // Encrypt  the content of the note
        String contenuACrypter = "";
 		try {
 			contenuACrypter = cryptService.encrypt(request.getContenu(), request.getIdUtilisateur());
 			request.setContenu(contenuACrypter);
 		} catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
 			// TODO Auto-generated catch block
 			logger.error("Encryption bug!");
 			e.printStackTrace();
 			return entity;
 		}
      
 		// Send request for update note
        		
        Map<String, String> params = new HashMap<String, String>();
        String str = id.toString();
        
        params.put("id", str);
      
     // Send request for update note
        String requestUrl = "http://localhost:8081/api/notes?id="+id;  
        
        HttpEntity<ModifyNotesRequest> requestUpdate = new HttpEntity<>(request);
   
        ResponseEntity<Void> response = restTemplate.exchange(requestUrl, HttpMethod.PUT, requestUpdate, Void.class, params);

        if (response.getStatusCode() == HttpStatus.OK) {
            logger.debug("Note {} modified!", request.getId());
           
            return new EntityCreatedResponse(request.getId(), request.getIdUtilisateur()); 
        } else {
           
        	 logger.error("Note {} not modified!", request.getId());
        	return new EntityCreatedResponse(request.getId(), request.getIdUtilisateur()); 
        }
        

     
  }

  //Delete the note  id
  @DeleteMapping("") //("/{id}")
  public void delete(@RequestParam("id") Integer id) {
	                 
	    String requestUrl = "http://localhost:8081/api/notes/" + id;
	    Map<String, String> params = new HashMap<String, String>();
	
		params.put("id",  Integer.toString(id));
	    
	    this.restTemplate.delete(requestUrl, params);
	
		logger.debug("Note deleted!") ;
  }
  @DeleteMapping("/{id}/user")
  public void deleteByUtilisateurId(@PathVariable("id") Integer id) {
	                 
	    String requestUrl = "http://localhost:8081/api/notes/" + id + "/user";
	    Map<String, String> params = new HashMap<String, String>();
	
		params.put("id",  Integer.toString(id));
	    
	    this.restTemplate.delete(requestUrl, params);
	
		logger.debug("Note deleted!") ;
  }
   
}
