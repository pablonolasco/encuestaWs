package mx.com.udemy.encuestasBackend.dto;

import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import mx.com.udemy.encuestasBackend.annotations.UniqueEmail;

//@Data // se usa apartir de la version 11 de jdk
public class UsuarioRegistroDTO {


	@NotEmpty
	private String nombre;

	@NotEmpty
	@Email
	@UniqueEmail
	private String email;

	@Size(min =2 ,max=8)
	@NotEmpty
	private String password;


	public String getNombre() {
		return nombre;
	}


	public void setNombre(String nombre) {
		this.nombre = nombre;
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
