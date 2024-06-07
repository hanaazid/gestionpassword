package fr.formation.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


@Entity
@Table(name = "utilisateur" )
public class Utilisateur {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	private String id;
	@Column(nullable=false)
	private String name;
	@Column(nullable=false)
	private LocalDateTime dateBirth;
	@Column(nullable=false)
	private String email;
	@Column(nullable=false)
	private String password;
	@Column(name="dateinscription")
	private LocalDateTime DateInscription;
	
	
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public LocalDateTime getDateInscription() {
		return DateInscription;
	}
	public void setDateInscription(LocalDateTime dateInscription) {
		DateInscription = dateInscription;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public LocalDateTime getDateNaissance() {
		return dateBirth;
	}
	public void setDateNaissance(LocalDateTime dateNaissance) {
		this.dateBirth = dateNaissance;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

}

