package fr.formation.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CreateCompteRequest {
	
    @NotNull
    private Integer utilisateurId;
    
    @NotNull
    private String platformname;
   
    @NotNull
    private String description;
    
    @NotBlank
    private String userName;
    
    @NotNull
    private String email;
    
    @NotNull
    private String adressUrl;
    
    @NotNull
    private String password;
    
   
    

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

	public String getUserName() {
		return userName;
	}

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

	public Integer getUtilisateurId() {
		return utilisateurId;
	}

	public void setUtilisateurId(Integer utilisateurId) {
		this.utilisateurId = utilisateurId;
	}

    

}
