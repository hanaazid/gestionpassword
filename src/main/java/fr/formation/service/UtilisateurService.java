package fr.formation.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.stereotype.Service;

import fr.formation.model.RoleEnum;
import fr.formation.model.Utilisateur;
import fr.formation.repo.UtilisateurRepository;
import fr.formation.request.CreateUserRequest;


@Service
public class UtilisateurService {

	@Autowired
	private UtilisateurRepository utilisateurRepository;
	
	@Autowired
	private PasswordUtils pwdService;

	private List<Utilisateur> utilisateurs = new ArrayList<>();
	private static final String MOT_DE_PASSE_FORT_REGEX = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";

	// Méthode pour inscrire un nouvel utilisateur
	public Utilisateur inscrireUtilisateur(CreateUserRequest utilisateur) {

		if (utilisateur.getPassword() == null || utilisateur.getPassword().isEmpty()) {
			throw new IllegalArgumentException("Le mot de passe ne peut pas être vide");
		}
		if (utilisateur.getEmail() == null || utilisateur.getEmail().isEmpty()) {
			throw new IllegalArgumentException("L'email ne peut pas être vide");
		}
		if (utilisateur.getName() == null || utilisateur.getName().isEmpty()) {
			throw new IllegalArgumentException("Le nom ne peut pas être vide");
		}

		// Vérifier si l'e-mail est déjà utilisé
		if (emailExisteDeja(utilisateur.getEmail())) {
			throw new IllegalArgumentException("Cet e-mail est déjà utilisé.");
		}

		// Vérifier la force du mot de passe
		if (!utilisateur.getPassword().isEmpty()) {
			verifierForceMotDePasse(utilisateur.getPassword());
		} 
		else {
			utilisateur.setPassword(genererMotDePasseFort());	
		}
		// Définir la date d'inscription
		
		// Ajouter l'utilisateur à la liste
		
		
		//utilisateurs.add(utilisateur);
		Utilisateur user = new Utilisateur();
		BeanUtils.copyProperties(utilisateur, user);
		user.setDateInscription(LocalDateTime.now());
		//Default value of user'rile is USER
		if (user.getRole() == null ) {
				user.setRole(RoleEnum.USER);
		}
		// hasher le password 
		System.out.println("in enregsitrement password : " + user.getPassword() );
		user.setPassword(pwdService.hashPassword(user.getPassword()));
		System.out.println("in enregsitrement password after hash: " + user.getPassword() );
		user = utilisateurRepository.save(user);
		return user;
		
	}

	// Méthode pour vérifier si l'e-mail est déjà utilisé
	private boolean emailExisteDeja(String email) {
		for (Utilisateur utilisateur : utilisateurs) {
			if (utilisateur.getEmail().equals(email)) {
				return true;
			}
		}
		return false;
	}

	// Méthode pour vérifier la force du mot de passe
	private void verifierForceMotDePasse(String password) {
		if (!Pattern.matches(MOT_DE_PASSE_FORT_REGEX, password)) {
			throw new IllegalArgumentException("Le mot de passe n'est pas assez fort.");
		}
	}

	// Méthode pour générer un mot de passe fort
	private String genererMotDePasseFort() {
		// Implémentez votre logique de génération de mot de passe fort ici
		return "MotDePasseFort123@";
	}

	// Méthode pour connecter un utilisateur
	public Utilisateur connecterUtilisateur(String email, String password) {

		List<Utilisateur> utilisateurs = utilisateurRepository.findAll();
		
		for (Utilisateur utilisateur : utilisateurs) {
			if (utilisateur.getEmail().equals(email) && utilisateur.getPassword().equals(password)) {
				return utilisateur;
			}
		}
		throw new IllegalArgumentException("Adresse e-mail ou mot de passe incorrect.");
	}

	// Méthode pour récupérer un mot de passe oublié
	public void recupererMotDePasseOublie(String email) {
		// Trouver l'utilisateur par email
		for (Utilisateur utilisateur : utilisateurs) {
			if (utilisateur.getEmail().equals(email)) {
				// Générer un nouveau mot de passe temporaire
				String motDePasseTemporaire = genererMotDePasseFort();
				utilisateur.setPassword(motDePasseTemporaire);

				// Envoyer un email à l'utilisateur avec le nouveau mot de passe temporaire
				System.out.println("Un email avec un mot de passe temporaire a été envoyé à " + email);
				System.out.println("Votre nouveau mot de passe temporaire est : " + motDePasseTemporaire);
				return;
			}
		}
		throw new IllegalArgumentException("Adresse e-mail non trouvée.");
	}

	public Iterable<Utilisateur> getListeUtilisateursMasques() {
		for (Utilisateur utilisateur : utilisateurs) {
			// Masquer le mot de passe pour chaque utilisateur
			utilisateur.setPassword("********"); // ou tout autre valeur de masquage que vous préférez
		}
		return utilisateurs;
	}


	public Utilisateur getUtilisateur(Integer id) {
		return utilisateurRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Utilisateur non trouvé."));
	}
	// Delete of the user
	public void deleteUtilisateur(Integer id) {
		 utilisateurRepository.deleteById(id);
	}
}








