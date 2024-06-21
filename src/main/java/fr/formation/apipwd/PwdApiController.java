package fr.formation.apipwd;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import fr.formation.apinotes.NotesApiController;
import fr.formation.model.Password;
import fr.formation.model.Utilisateur;
import fr.formation.notes.Notes;
import fr.formation.repo.UtilisateurRepository;
import fr.formation.service.PasswordConvertService;

@RestController
@RequestMapping("/api/user/password")
@CrossOrigin("*")
public class PwdApiController {
	
	@Autowired
	public RestTemplate restTemplate; 
	
	@Autowired
    public UtilisateurRepository repo;
	
	@Autowired
	public PasswordConvertService pwdService;
	
	private static final Logger logger = LoggerFactory.getLogger(PwdApiController.class);
	
	@GetMapping("/{idUtilisateur}")  
	public Boolean getNoteById(@PathVariable int idUtilisateur, 
			@RequestParam  String password ){
		
        String pwdCrypte = "";
		try {
			pwdCrypte = pwdService.encrypt(password, idUtilisateur);
		} catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String requestUrl = "http://localhost:8083//api/password/" + pwdCrypte;
		System.out.println(pwdCrypte);
		
		Map<String, String> params = new HashMap<String, String>();
		params.put("password", pwdCrypte);

		RestTemplate restTemplate = new RestTemplate();
	
		Boolean result = restTemplate.getForObject(requestUrl, Boolean.class, params);

		return result;
	}

}
