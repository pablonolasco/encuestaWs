package mx.com.udemy.encuestasBackend.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import mx.com.udemy.encuestasBackend.validators.UniqueEmailValidator;
// indica de donde obtendra la validacion
@Constraint(validatedBy = UniqueEmailValidator.class)
@Target(ElementType.FIELD)// solamente se usara para campos
@Retention(RetentionPolicy.RUNTIME)// se ejecutara en tiempo de ejecucion
public @interface UniqueEmail {
	String message() default "{encuesta.constraints.email.unique.message}";
	
	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

}
