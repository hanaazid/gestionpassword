package fr.formation.request;

import java.time.LocalDate;

public class CreateUserRequest {
	
    private String name;
	
	private LocalDate dateBirth;

	private String email;

	private String motPrimaire;
	
	private String password;

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

	public String getMotPrimaire() {
		return motPrimaire;
	}

	public void setMotPrimaire(String motPrimaire) {
		this.motPrimaire = motPrimaire;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	
	

}
