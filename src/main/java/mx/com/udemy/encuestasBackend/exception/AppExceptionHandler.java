package mx.com.udemy.encuestasBackend.exception;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class AppExceptionHandler {

	@ExceptionHandler(value = {MethodArgumentNotValidException.class})
	public ResponseEntity<Object>handleValidationErrorException(MethodArgumentNotValidException ex, WebRequest webRequest){
		
		Map<String, String>errors=new HashMap<>();
		for(ObjectError error: ex.getBindingResult().getAllErrors()) {
			String fieldName=((FieldError)error).getField();
			String errorMessage=error.getDefaultMessage();
			errors.put(fieldName, errorMessage);
		}
		
		ValidationErrors validationErrors= new ValidationErrors(errors, LocalDateTime.now());
		
		return new ResponseEntity<>(validationErrors, new HttpHeaders(),HttpStatus.BAD_REQUEST);
	}
}
