package fr.formation.request;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class UtilisateurRequest {

	    @NotBlank
	    private String name;

	    @NotNull
	    private LocalDateTime dateBirth;

	    @NotBlank
	    private String email;

	    @NotBlank
	    private String password;



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

}
