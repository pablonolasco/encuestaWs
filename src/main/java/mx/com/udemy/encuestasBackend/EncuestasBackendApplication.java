package mx.com.udemy.encuestasBackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import mx.com.udemy.encuestasBackend.security.AppProperties;

@SpringBootApplication
public class EncuestasBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(EncuestasBackendApplication.class, args);
		System.out.print("Funcionando");
	}
	
	/**
	 * Registramos BCryptPasswordEncoder como bean para que se pueda usar en todo el proyecto 
	 * @return {@link BCryptPasswordEncoder } clase que encripta la contraseña
	 */
	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	/**
	 * Bean global para poder acceder a las variables de configuracion del archivo application.properties
	 * @return {@link AppProperties}
	 */
	@Bean(name = "AppProperties")
	public AppProperties getAppProperties() {
		return new AppProperties();
	}
	
	/**
	 * Bean para poder acceder de manera global
	 * @return
	 */
	@Bean
	public SpringApplicationContext springApplicationContext() {
		return new SpringApplicationContext();
	}

}
