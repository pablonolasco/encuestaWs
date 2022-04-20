package mx.com.udemy.encuestasBackend.services;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import mx.com.udemy.encuestasBackend.dto.UsuarioRegistroDTO;
import mx.com.udemy.encuestasBackend.entities.Usuario;

public interface IUserService extends UserDetailsService{

	/**
	 * Metodo para registrar un usuario
	 * @param registroDTO {@link UsuarioRegistroDTO} objeto a guardar
	 * @return Usuario {@link Usuario}
	 */
	public Usuario crearUsuario(UsuarioRegistroDTO registroDTO);
	
	public Usuario obtenerUsuario(String email);
	
	// metodo que se usa para el logueo con spring security y jwt
	/**
	 * Metodo sobreescrito de la clase UserDetailsService para auntenticar el usuario por email
	 */
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException;
	

}
