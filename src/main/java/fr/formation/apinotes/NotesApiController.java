package fr.formation.apinotes;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import fr.formation.notes.Notes;

@RestController
@RequestMapping("/api/user/notes")
@CrossOrigin("*")
public class NotesApiController {

	@Autowired
	public RestTemplate restTemplate; 

	@GetMapping("/{idUtilisateur}")
	public List<Notes> getNoteById(@PathVariable int idUtilisateur)
	{
		String requeste = "http://localhost:8081/api/notes/" + idUtilisateur;
		System.out.println(requeste);
        ResponseEntity<List<Notes>> response = this.restTemplate
                .exchange(requeste,   
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<List<Notes>>() {});
        
		return response.getBody();
	}
}
