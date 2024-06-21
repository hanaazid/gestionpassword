package fr.formation.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class NotesService {

    private final RestTemplate restTemplate;

    @Value("${notes.service.url}")
    private String notesServiceUrl;

    @Autowired
    public NotesService(@Qualifier("customRestTemplate") RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void deleteNotesByUserId(Integer userId) {
    	// notes.service.url= http://localhost:8081 - /api/notes/{id}/user
        String url = notesServiceUrl + "/api/notes/" + userId+"/user";
        
        System.out.println("delete by user id");
        restTemplate.delete(url);
    }
    
    
}
