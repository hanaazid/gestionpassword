package fr.formation.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.hibernate.annotations.UuidGenerator;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;


@Entity
@Table(name = "utilisateur" )
public class Utilisateur {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@Column(nullable=false)
	private String name;
	@Column(nullable=false)
	@Enumerated(EnumType.STRING)
	private RoleEnum role;
	@Column(name="dateBirth")
	private LocalDate dateBirth;
	
	@Column(nullable=false)
	private String email;
	@Column(nullable=false)
	private String password;
	@Column(nullable=false, name="motprimaire")
	private String motPrimaire;
	@Column(name="dateinscription")
	private LocalDateTime DateInscription;
	@JsonIgnoreProperties("utilisateur") // Ignorer la sérialisation de la propriété "utilisateur" dans Compte
	@OneToMany(mappedBy = "utilisateur")
	private List<Compte> compteplatforms;
	
	//private List<Notes> listNotes;
	
	 public Utilisateur() {}
	 
	

	public RoleEnum getRole() {
		return role;
	}



	public void setRole(RoleEnum role) {
		this.role = role;
	}



	public Integer getId() {
		return id;
	}



	public void setId(Integer id) {
		this.id = id;
	}



	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public LocalDate getDateBirth() {
		return dateBirth;
	}
	public void setDateBirth(LocalDate dateBirth) {
		this.dateBirth = dateBirth;
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
	public String getMotPrimaire() {
		return motPrimaire;
	}

	public void setMotPrimaire(String motPrimaire) {
		this.motPrimaire = motPrimaire;
	}
	public LocalDateTime getDateInscription() {
		return DateInscription;
	}
	public void setDateInscription(LocalDateTime dateInscription) {
		DateInscription = dateInscription;
	}
	public List<Compte> getCompteplatforms() {
		return compteplatforms;
	}
	public void setCompteplatforms(List<Compte> compteplatforms) {
		this.compteplatforms = compteplatforms;
	}
	
}
	
	