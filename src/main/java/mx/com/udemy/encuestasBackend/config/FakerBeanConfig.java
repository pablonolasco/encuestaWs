package mx.com.udemy.encuestasBackend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.javafaker.Faker;

@Configuration
public class FakerBeanConfig {
	
	// bean explicito para poder inyectar la clase faker en cualquier lado sin instanciar la clase
	@Bean
	public Faker getFaker() {
		return new Faker();
	}
}
