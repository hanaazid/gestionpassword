package fr.formation.response;

public class CompteResponse {
	
	
	
	private String Id;
	private String password;
	private String email;
	private String platformname;
	private String adressUrl;
	private String description;
	
	
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
	public String getPlatformname() {
		return platformname;
	}
	public void setPlatformname(String platformname) {
		this.platformname = platformname;
	}
	public String getAdressUrl() {
		return adressUrl;
	}
	public void setAdressUrl(String adressUrl) {
		this.adressUrl = adressUrl;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	

}
