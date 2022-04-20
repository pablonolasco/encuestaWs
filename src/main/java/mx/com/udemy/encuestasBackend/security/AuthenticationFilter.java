package mx.com.udemy.encuestasBackend.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import mx.com.udemy.encuestasBackend.dto.UsuarioLoginDTO;

/**
 * Filtro para autenticar el usuario en el sistema, se especifica el tipo de
 * autenticacion que el sistema utiliza para iniciar sesion, en nuestro caso con
 * usuario y contraseña y se genera el token JWT. Busca en la base de datos el
 * usuario y contraseña, si es correcto genera el token y lo regresa.
 * 
 * @author pnolasco
 *
 */
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	private final AuthenticationManager authenticationManager;
	
	private static final Logger log = LoggerFactory.getLogger(AuthenticationFilter.class);


	public AuthenticationFilter(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		try {
			UsuarioLoginDTO usuarioLoginDTO = new ObjectMapper().readValue(request.getInputStream(),
					UsuarioLoginDTO.class);
			// == pero tambien se puede enviar un campo usuario o email y password, el array se coloca por si se quiere enviar una opcion adicional, en este caso se manda solo declarado
			return authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(usuarioLoginDTO.getEmail(), usuarioLoginDTO.getPassword(),new ArrayList<>()));

		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
	}

	/**
	 * si la autenticacion es exitosa genera el token
	 */
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		String email=((User) authResult.getPrincipal()).getUsername();
		// generar token
		String token=Jwts.builder()
				.setSubject(email)
				.setExpiration(new Date(System.currentTimeMillis()+SecurityConstants.EXPIRATION_DATE))
				.signWith(SignatureAlgorithm.HS512, SecurityConstants.getTokenSecret())
				.compact();
		//log.info("token {}",token);
		Map<String, String>credentialesToken=new HashMap<>();
		credentialesToken.put("token", SecurityConstants.TOKEN_PREFIX.concat(" ").concat(token));
		String data= new ObjectMapper().writeValueAsString(credentialesToken);
		
		// ==regresa el token generado a la peticion
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().print(data);
		response.flushBuffer();
	}
	
	

}
