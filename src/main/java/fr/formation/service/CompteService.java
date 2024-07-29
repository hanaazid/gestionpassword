package fr.formation.service;

import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.crypto.NoSuchPaddingException;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import fr.formation.model.Compte;
import fr.formation.model.Utilisateur;
import fr.formation.repo.CompteRepository;
import fr.formation.repo.UtilisateurRepository;
import fr.formation.request.CreateCompteRequest;

@Service
public class CompteService {

    @Autowired
    private CompteRepository compteRepository;

    @Autowired
    private UtilisateurRepository utilisateurRepository;
    
    
    @Autowired
    public PasswordConvertService pwdService;

    public List<Compte> getAllComptes() {
    	
    	//System.out.println("in enregsitrement password : " + user.getPassword() );
		//user.setPassword(pwdService.hashPassword(user.getPassword()));
		List<Compte> lcompte =  compteRepository.findAll();
		String pwdTmp = "";
		String motPrimaire = "";
		for (Compte compte : lcompte) {
			pwdTmp = compte.getPassword();
			try {
				motPrimaire = compte.getUtilisateur().getMotPrimaire();
				pwdTmp = pwdService.decryptWithString(pwdTmp, motPrimaire);
			} catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			pwdTmp = pwdService.removeEvenIndexedChars(pwdTmp);
    		compte.setPassword(pwdTmp);
    	
		
        }
		return lcompte;
    }

    public Compte getCompteById(Integer id) {
    	
        //return compteRepository.findById(id)
        //        .orElseThrow(() -> new RuntimeException("Compte non trouvé avec ID: " + id));
    	
    	Compte compte = compteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Compte non trouvé avec ID: " + id));

        // Déchiffrement et suppression des caractères paires
        String pwdTmp = compte.getPassword();
        try {
            // Déchiffrement
            pwdTmp = pwdService.decryptWithString(pwdTmp, compte.getUtilisateur().getMotPrimaire());
            
            // Suppression des caractères paires
            pwdTmp = pwdService.removeEvenIndexedChars(pwdTmp);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            e.printStackTrace();
            throw new RuntimeException("Erreur lors du traitement du mot de passe", e);
        }

        // Mise à jour du mot de passe dans l'objet Compte
        compte.setPassword(pwdTmp);

        return compte;
        
    }

    public Integer addCompte(CreateCompteRequest compteRequest) {
        Utilisateur utilisateur = utilisateurRepository.getById(compteRequest.getUtilisateurId());
        Compte compte = new Compte();
        BeanUtils.copyProperties(compteRequest, compte);
        String pwdTmp = compte.getPassword();
        String motPrimaire = utilisateur.getMotPrimaire();
        System.out.println("mot primaire" + motPrimaire);
        
        try {
        	// ajout de chiffre aléatoire en position paire
        	pwdTmp = pwdService.insertRandomDigits(pwdTmp);
            // Déchiffrement
            pwdTmp = pwdService.encryptWithString(pwdTmp, motPrimaire);
            
           
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            e.printStackTrace();
            throw new RuntimeException("Erreur lors du traitement du mot de passe", e);
        }
        compte.setPassword(pwdTmp);
        compte.setDateAdded(LocalDateTime.now());
        compte.setDateUpdate(LocalDateTime.now());
        compte.setUtilisateur(utilisateur);
        compteRepository.save(compte);
        return compte.getId();
    }

    public Compte updateCompte(Compte compte) {
        if (compte.getId() == null) {
            throw new RuntimeException("Besoin d'un id pour faire une mise à jour.");
        }

        Compte existingCompte = getCompteById(compte.getId());
        String pwdTmp = compte.getPassword();
        // Copie les propriétés mises à jour, mais conserve certaines anciennes propriétés
        existingCompte.setPlatformname(compte.getPlatformname());
        existingCompte.setDescription(compte.getDescription());
        existingCompte.setUserName(compte.getUserName());
        existingCompte.setEmail(compte.getEmail());
        existingCompte.setAdressUrl(compte.getAdressUrl());
        
        try {
        	// ajout de chiffre aléatoire en position paire
        	pwdTmp = pwdService.insertRandomDigits(pwdTmp);
            // Déchiffrement
            pwdTmp = pwdService.encryptWithString(pwdTmp, compte.getUtilisateur().getMotPrimaire());
            
           
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            e.printStackTrace();
            throw new RuntimeException("Erreur lors du traitement du mot de passe", e);
        }
        existingCompte.setPassword(pwdTmp);
        
        existingCompte.setDateUpdate(LocalDateTime.now());

        return compteRepository.save(existingCompte);
    }

    public boolean existsCompteById(Integer id) {
        return compteRepository.existsById(id);
    }

    public void deleteCompte(Integer id) {
        if (compteRepository.existsById(id)) {
            compteRepository.deleteById(id);
        }
    }

    public List<Compte> findByUtilisateurId(Integer idUtilisateur) {
    	
    	List<Compte> lcompte =   compteRepository.findByUtilisateurId(idUtilisateur);
    	
    	String pwdTmp = "";
    	Utilisateur user = utilisateurRepository.getById(idUtilisateur);
    	String motPrimaire = user.getMotPrimaire();
		for (Compte compte : lcompte) {
			pwdTmp = compte.getPassword();
			try {
				pwdTmp = pwdService.decryptWithString(pwdTmp, motPrimaire);
			} catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			pwdTmp = pwdService.removeEvenIndexedChars(pwdTmp);
    		compte.setPassword(pwdTmp);
    	
		
        }
        
        return lcompte;
    }
}
