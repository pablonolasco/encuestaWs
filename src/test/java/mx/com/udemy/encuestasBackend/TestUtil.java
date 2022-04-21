package mx.com.udemy.encuestasBackend;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;

import mx.com.udemy.encuestasBackend.dto.UsuarioRegistroDTO;
import mx.com.udemy.encuestasBackend.security.SecurityConstants;

public class TestUtil {

	
	
	public static UsuarioRegistroDTO crearUsuarioRegistroDTO() {
		UsuarioRegistroDTO usuarioRegistroDTO= new UsuarioRegistroDTO();
		usuarioRegistroDTO.setEmail(SecurityConstants.getFackerDatos().name().username()+"@gmail.com");
		usuarioRegistroDTO.setNombre(SecurityConstants.getFackerDatos().name().firstName());
		usuarioRegistroDTO.setPassword(generateRandomString(8));
		/*usuarioRegistroDTO.setEmail(generateRandomString(8)+"@gmail.com");
		usuarioRegistroDTO.setNombre(generateRandomString(8));
		usuarioRegistroDTO.setPassword(generateRandomString(8));*/
		return usuarioRegistroDTO;
	}
	
	// genera cadena de caracteres aleatorios
	public static String generateRandomString(int len) {
		String chars="ABCDEFGHIJKLMNÑOPQRSTUVWXYZabcdefghijklmnñopqrstuvwxyz";
		Random random= new Random();
		
		StringBuilder sb= new StringBuilder(len);
		
		for (int i = 0; i < len; i++) {
			sb.append(chars.charAt(random.nextInt(chars.length())));
		}
		
		return sb.toString();
		
	}
}
