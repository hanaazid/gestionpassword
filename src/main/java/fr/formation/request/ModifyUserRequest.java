package fr.formation.request;

import java.time.LocalDateTime;

public class ModifyUserRequest {

	private String name;
	
	private LocalDateTime dateBirth;

	private String email;

	private String password;

	private String motPrimaire;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public LocalDateTime getDateBirth() {
		return dateBirth;
	}

	public void setDateBirth(LocalDateTime dateBirth) {
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
	
	
}
