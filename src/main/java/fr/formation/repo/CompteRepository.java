package fr.formation.repo;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import fr.formation.model.Compte;
import fr.formation.model.Utilisateur;

public interface CompteRepository extends JpaRepository<Compte, Integer> {
	
	boolean existsById(Integer id);
	
	List<Compte> findByUtilisateurId(Integer idUtilisateur);


}
