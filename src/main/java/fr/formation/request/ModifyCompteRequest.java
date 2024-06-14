package fr.formation.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ModifyCompteRequest {

	private Integer id;
	
	@NotBlank
	private String platformname;

	@NotNull
	private String description;

	@NotBlank
	private String userName;

	@NotBlank
	private String email;

	@NotNull
	private String adressUrl;

	@NotBlank
	private String password;
	
	
	

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


}
