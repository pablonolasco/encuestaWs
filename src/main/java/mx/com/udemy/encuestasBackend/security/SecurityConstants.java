package mx.com.udemy.encuestasBackend.security;

import mx.com.udemy.encuestasBackend.SpringApplicationContext;

/**
 * Clase de constantes que se usan en la autenticacion con jwt
 * @author pnolasco
 *
 */
public class SecurityConstants {

	public static final Long EXPIRATION_DATE=864000000L; // 10 dias en mls
	public static final String LOGIN_URL="/users/login"; // 10 dias en mls
	public static final String TOKEN_PREFIX="Bearer"; // 10 dias en mls
	public static final String HEADER_STRING="Authorization"; // 10 dias en mls
	
	public static String getTokenSecret() {
		AppProperties appProperties=(AppProperties) SpringApplicationContext.getBean("AppProperties");
		return appProperties.getTokenSecret();
	}
	
	
}
