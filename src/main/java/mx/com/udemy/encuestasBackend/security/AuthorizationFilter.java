package mx.com.udemy.encuestasBackend.security;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import io.jsonwebtoken.Jwts;

/**
 * Filtro para autorizar el usuario dentro del sistema, cuando se envia el
 * token, este filtro debe validar que el token sea un token valido y que haya
 * sido firmado con el algoritmo y clave secreta de la aplicacion. Valida si el
 * token enviado en la peticion es correcto
 * 
 * @author pnolasco
 *
 */
public class AuthorizationFilter extends BasicAuthenticationFilter {

	public AuthorizationFilter(AuthenticationManager authenticationManager) {
		super(authenticationManager);
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		// == recuperar el token autorization header
		String header = request.getHeader(SecurityConstants.HEADER_STRING);

		// == validar estructura del token
		if (header == null || !header.startsWith(SecurityConstants.TOKEN_PREFIX)) {
			chain.doFilter(request, response);
			return;
		}

		// == regresa un usuario
		UsernamePasswordAuthenticationToken authenticationToken = getAuthenticationToken(request);

		// == setear el contexto para el usuario logueado
		SecurityContextHolder.getContext().setAuthentication(authenticationToken);

		chain.doFilter(request, response);
	}

	// == validar token
	/**
	 * Metodo para validar el token del request
	 * @param request {@link HttpServletRequest} request de la peticion
	 * @return {@link UsernamePasswordAuthenticationToken} usuario
	 */
	private UsernamePasswordAuthenticationToken getAuthenticationToken(HttpServletRequest request) {
		String token = request.getHeader(SecurityConstants.HEADER_STRING);
		if (token != null) {
			// == quitar la palabra bearer
			token = token.replace(SecurityConstants.TOKEN_PREFIX, "");
			// == obtener email del token
			String email = Jwts.parser().setSigningKey(SecurityConstants.getTokenSecret()).parseClaimsJws(token)
					.getBody().getSubject();
			if (email != null) {
				return new UsernamePasswordAuthenticationToken(email, null, new ArrayList<>());
			}
			return null;
		}
		return null;
	}

}
