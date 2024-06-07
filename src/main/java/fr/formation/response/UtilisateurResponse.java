package fr.formation.response;

import java.time.LocalDateTime;

public class UtilisateurResponse {

	private String Id;
	private String password;
	private String email;
	private LocalDateTime dateInscription;
	public String getId() {
		return Id;
	}
	public void setId(String id) {
		Id = id;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public LocalDateTime getDateInscription() {
		return dateInscription;
	}
	public void setDateInscription(LocalDateTime dateInscription) {
		this.dateInscription = dateInscription;
	}
	
	


	
}
