package fr.formation.service;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.NoSuchPaddingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.BeanUtils;

import fr.formation.model.Utilisateur;
import fr.formation.notes.Notes;	
import fr.formation.notes.NotesList;
import fr.formation.repo.UtilisateurRepository;
import fr.formation.request.ModifyUserRequest;
import fr.formation.notes.ModifyNotesRequest;


@Service
public class NotesService {

    private final RestTemplate restTemplate;

    @Value("${notes.service.url}")
    private String notesServiceUrl;

    @Autowired
    public NotesService(@Qualifier("customRestTemplate") RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
    
    @Autowired
    public PasswordConvertService pwdService;
    
    @Autowired
    public UtilisateurRepository userRepo;

    public void deleteNotesByUserId(Integer userId) {
    	// notes.service.url= http://localhost:8081 - /api/notes/{id}/user
        String url = notesServiceUrl + "/api/notes/" + userId+"/user";
        // delete all note associated the user userId
        //System.out.println("delete by user id");
        restTemplate.delete(url);
    }
    
    // If the motPrimaire is changed then
    public void modifyNotesByUserId(Integer userId ,  String oldMotPrimaire, String motPrimaire) {
    	
    	// url = http://localhost:8081/api/notes/{userId}/allnotesuser
    	String url = notesServiceUrl + "/api/notes/" + userId+"/allnotesuser";
    	String urlPut = notesServiceUrl + "/api/notes" ;

    	String contenuTmp = "";
    	String contenuClear = "";
    		
    	 ResponseEntity<List<Notes>> response = restTemplate.exchange(
    		        url, 
    		        HttpMethod.GET, 
    		        null, 
    		        new ParameterizedTypeReference<List<Notes>>() {}
    		    );
    	
    	 //Fetch the notes associated to the user userId
    	List<Notes> lnotes =  response.getBody();
    	if (lnotes == null ) {
    		System.out.println("lnotes null");
            return;
        }
    	
    	for ( Notes note : lnotes )	{
    		// Decrypt and Encrypt the notes's content
    		//Map the params
    		Map<String, String> params = new HashMap<>();
        	params.put("id", String.valueOf(note.getId()));
            contenuTmp = note.getContenu();
            
    		//Decrypt the content's note with the old user's motPrimaire
    		try {
				contenuClear = pwdService.decryptWithString(contenuTmp, oldMotPrimaire);
			} catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		
    		//Encrypt with new user motPrimaire 
    		try {
				contenuTmp = pwdService.encryptWithString(contenuClear, motPrimaire);
			} catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		
    		note.setContenu(contenuTmp);
    		ModifyNotesRequest request = new ModifyNotesRequest();
    		
    	    BeanUtils.copyProperties(note, request);
    	    
    	    //Update the note
    	    urlPut = notesServiceUrl + "/api/notes?id=" +  note.getId();
    	    restTemplate.put(urlPut, request, params);
    	    
    		
    	}
    	
    }
    
    
}
