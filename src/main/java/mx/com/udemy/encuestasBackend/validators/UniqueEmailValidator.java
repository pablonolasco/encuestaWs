package mx.com.udemy.encuestasBackend.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import mx.com.udemy.encuestasBackend.annotations.UniqueEmail;
import mx.com.udemy.encuestasBackend.entities.Usuario;
import mx.com.udemy.encuestasBackend.repository.IUsuarioRepository;

public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, String> {

	@Autowired
	private IUsuarioRepository usuarioRepository;
	
	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		Usuario usuario=usuarioRepository.findByEmail(value);
		if (usuario == null) {
			return true;
		}
		return false;
	}

}
