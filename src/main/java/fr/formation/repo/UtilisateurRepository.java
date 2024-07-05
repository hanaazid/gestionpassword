package fr.formation.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.formation.model.Utilisateur;

public interface UtilisateurRepository extends JpaRepository<Utilisateur, Integer> {
	
	Utilisateur findByEmail(String email);
	Optional<Utilisateur> findByEmailAndPassword(String email, String password);

}
