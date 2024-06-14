package fr.formation.model;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;



@Entity
@Table(name="compte")
@JsonIgnoreProperties("utilisateur") // Ignorer la sérialisation de la propriété "utilisateur"
public class Compte {//compteplateforme

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@Column(nullable=false)
	private String platformname;
	@Column(nullable=false)
	private String description;
	@Column(nullable=false)
	private LocalDateTime dateAdded;
	@Column(nullable=false)
	private LocalDateTime dateUpdate;
	@Column(nullable=false)
	private String userName;
	@Column(nullable=false)
	private String email;
	@Column(nullable=false)
	private String adressUrl;
	@Column(nullable=false)
	private String password;
	@ManyToOne
	@JoinColumn(name = "utilisateur_id", nullable = false)
	private Utilisateur utilisateur;


	public Compte() {}
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getPlatformname() {
		return platformname;
	}
	public void setPlatformname(String platformname) {
		this.platformname = platformname;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public LocalDateTime getDateAdded() {
		return dateAdded;
	}
	public void setDateAdded(LocalDateTime dateAdded) {
		this.dateAdded = dateAdded;
	}
	public LocalDateTime getDateUpdate() {
		return dateUpdate;
	}
	public void setDateUpdate(LocalDateTime dateUpdate) {
		this.dateUpdate = dateUpdate;
	}
	public String getUserName() {
		return userName;
	}

	/*
	 * public List<Notes> getListNotes() { return listNotes; } public void
	 * setListNotes(List<Notes> listNotes) { this.listNotes = listNotes; }
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getAdressUrl() {
		return adressUrl;
	}
	public void setAdressUrl(String adressUrl) {
		this.adressUrl = adressUrl;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}


	public Utilisateur getUtilisateur() {
		return utilisateur;
	}


	public void setUtilisateur(Utilisateur utilisateur) {
		this.utilisateur = utilisateur;
	}


}