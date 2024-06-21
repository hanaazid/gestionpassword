package fr.formation.service;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.formation.model.Utilisateur;
import fr.formation.repo.UtilisateurRepository;

@Service
public class PasswordConvertService {
	
	@Autowired
    public UtilisateurRepository repo;
	
	public String encrypt(String password,Integer idUtilisateur) throws NoSuchAlgorithmException, NoSuchPaddingException{
		// Generate a secret key
		Utilisateur user = new Utilisateur();
		user = repo.getReferenceById(idUtilisateur);
		String motPrimaire = user.getMotPrimaire();
		System.out.println(motPrimaire);
		byte[] keyData = motPrimaire.getBytes();
		
		// Build the SecretKeySpec using Blowfish algorithm
		SecretKeySpec secretKeySpec = new SecretKeySpec(keyData, "Blowfish");

		// Build the cipher using Blowfish algorithm
		Cipher cipher = Cipher.getInstance("Blowfish");

		// Initialize cipher in encryption mode with secret key
		
		try {
			cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// the text to encrypt
		String secretMessage = password;

		// encrypt message
		byte[] encryptedBytes = null;
		try {
			encryptedBytes = cipher.doFinal(secretMessage.getBytes(StandardCharsets.UTF_8));
		} catch (IllegalBlockSizeException | BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// encode with Base64 encoder
		String encryptedtext = Base64.getEncoder().encodeToString(encryptedBytes);
		System.out.println( encryptedtext);
		return encryptedtext;
	}
	
      public String decrypt (String encryptedText,Integer idUtilisateur)throws NoSuchAlgorithmException, NoSuchPaddingException{
    	// Generate a secret key
  		Utilisateur user = new Utilisateur();
  		user = repo.getReferenceById(idUtilisateur);
  		System.out.println( user.toString());
  		String motPrimaire = user.getMotPrimaire();
  	
  		
  	// Convert the primary key to byte array
  	    byte[] keyData = motPrimaire.getBytes(StandardCharsets.UTF_8);

  	    // Create the Blowfish Cipher
  	    Cipher cipher = Cipher.getInstance("Blowfish");

  	    // Build the SecretKeySpec using Blowfish algorithm
  	    SecretKeySpec secretKeySpec = new SecretKeySpec(keyData, "Blowfish");

  	    // Initialize cipher in decryption mode with secret key
  	    try {
  	        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
  	    } catch (InvalidKeyException e) {
  	        e.printStackTrace();
  	        throw new RuntimeException("Failed to initialize cipher", e);
  	    }

  	    // Decode using Base64 and decrypt the message
  	    byte[] decrypted = null;
  	    try {
  	        decrypted = cipher.doFinal(Base64.getDecoder().decode(encryptedText));
  	    } catch (IllegalBlockSizeException | BadPaddingException e) {
  	        e.printStackTrace();
  	        throw new RuntimeException("Decryption failed", e);
  	    }

  	    // Convert the decrypted bytes to String
  	    String decryptedString = new String(decrypted, StandardCharsets.UTF_8);
    	  
    
        return decryptedString;

    	  
      }

}
