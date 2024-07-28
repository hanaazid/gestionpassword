package fr.formation.service;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Random;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


import fr.formation.model.Utilisateur;
import fr.formation.repo.UtilisateurRepository;

@Service
public class PasswordConvertService {
	
	@Autowired
    public UtilisateurRepository repo;
	
	private static final Random random = new Random();
	
	public String encrypt_Sha_1(String password) {
		String hashedPassword = null;
		try {
            // Crée une instance de MessageDigest avec l'algorithme SHA-1
            MessageDigest md = MessageDigest.getInstance("SHA-1");

            // Convertit le mot de passe en tableau de bytes
            byte[] bytes = md.digest(password.getBytes());

            // Convertit les bytes en une chaîne hexadécimale
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) {
                sb.append(String.format("%02x", b));
            }
            hashedPassword = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return hashedPassword;
		
	}
	
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

      public String encryptWithString(String password,String motPrimaire) throws NoSuchAlgorithmException, NoSuchPaddingException{
  		// Generate a secret key

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
  	
        public String decryptWithString (String encryptedText,String motPrimaire)throws NoSuchAlgorithmException, NoSuchPaddingException{
      	// Generate a secret key
    		//Utilisateur user = new Utilisateur();
    		//user = repo.getReferenceById(idUtilisateur);
    		System.out.println( motPrimaire );
    	
    		
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
        

        //méthode de rajout d'un chiffre aléatoire en position paire avant chiffrement
        public   String insertRandomDigits(String password) {
            StringBuilder newPassword = new StringBuilder();
            for (char c : password.toCharArray()) {
                newPassword.append(c);
                int randomDigit = 1 + random.nextInt(9); // Génère un chiffre entre 1 et 9
                newPassword.append(randomDigit);
            }
            return newPassword.toString();
        }
        //méthode de suppression du caratères paire
        public   String removeEvenIndexedChars(String str) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < str.length(); i++) {
                if (i % 2 != 0) {
                    sb.append(str.charAt(i));
                }
            }
            return sb.toString();
        }

}
