package fr.formation.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.formation.model.Compte;

public interface CompteRepository extends JpaRepository<Compte, String> {
	
	boolean existsById(Integer id);


}
