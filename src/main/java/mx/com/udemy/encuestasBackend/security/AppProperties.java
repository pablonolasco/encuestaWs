package mx.com.udemy.encuestasBackend.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * Clase para obtener las variables de configuracion del archivo application.properties
 * @author pnolasco
 *
 */
@Component
public class AppProperties {

	// == variable de configuracion
	@Autowired
	private Environment env;
	
	/**
	 * Metodo que obtiene la secret key
	 * @return {@link app.jwt-secret} variable del archivo properties
	 */
	public String getTokenSecret() {
		return env.getProperty("app.jwt-secret");
	}
}
