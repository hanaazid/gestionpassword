package fr.formation.response;

public class EntityCreatedResponse {



	private Integer id;
	private Integer utilisateurId;
	
	
	public EntityCreatedResponse() {
		super();
	}
	
	public EntityCreatedResponse(Integer id, Integer utilisateurId) {
		super();
		this.id = id;
		this.utilisateurId = utilisateurId;
	}

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getUtilisateurId() {
		return utilisateurId;
	}
	public void setUtilisateurId(Integer utilisateurId) {
		this.utilisateurId = utilisateurId;
	}
	
	
	

}