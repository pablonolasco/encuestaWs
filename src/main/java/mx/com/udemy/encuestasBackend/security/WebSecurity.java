package mx.com.udemy.encuestasBackend.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import mx.com.udemy.encuestasBackend.services.IUserService;

@EnableWebSecurity // se coloca para indicar que la aplicacion tendra una configuracion de seguridad
public class WebSecurity extends WebSecurityConfigurerAdapter {

	private final IUserService usuarioServicio;
	
	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
	public WebSecurity(IUserService usuarioServicio, BCryptPasswordEncoder bCryptPasswordEncoder) {
		this.usuarioServicio = usuarioServicio;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// === deshabilita el cross porque es una api y cualquiera puede acceder a los recursos
		http.cors().and().csrf().disable();
		// === se deshabilita la autenticacion para esa ruta con el metodo post
		http.authorizeRequests().antMatchers(HttpMethod.POST, "/users").permitAll().anyRequest().authenticated();

		// ==añadir filtro a la configuracion de spring security
		// ==sessionCreationPolicy(SessionCreationPolicy.STATELESS) se coloca porque se usara token y no cockies o sessiones
		http.addFilter(getAuthenticationFilter())
			.addFilter(new AuthorizationFilter(authenticationManager()))
			.sessionManagement()
			.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
	}

	/**
	 * se configura el usuario en spring web security
	 */
	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		// == indicar el user service y la incriptacion del password
		auth.userDetailsService(usuarioServicio).passwordEncoder(bCryptPasswordEncoder);
	}
	
	/**
	 * añadir un filtro
	 */
	
	public AuthenticationFilter getAuthenticationFilter()throws Exception{
		final AuthenticationFilter authenticationFilter= new AuthenticationFilter(authenticationManager());
		// ==se escribe la nueva url de login
		authenticationFilter.setFilterProcessesUrl(SecurityConstants.LOGIN_URL);
		
		return authenticationFilter;
	}
	
	
	
	

}
