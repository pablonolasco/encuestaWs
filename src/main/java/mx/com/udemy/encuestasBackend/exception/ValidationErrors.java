package mx.com.udemy.encuestasBackend.exception;


import java.time.LocalDateTime;
import java.util.Map;

public class ValidationErrors {
	
	private Map<String, String>errors;
	
	private LocalDateTime timeStamp;

	public ValidationErrors(Map<String, String> errors, LocalDateTime timeStamp) {
		super();
		this.errors = errors;
		this.timeStamp = timeStamp;
	}

	public ValidationErrors() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Map<String, String> getErrors() {
		return errors;
	}

	public void setErrors(Map<String, String> errors) {
		this.errors = errors;
	}

	public LocalDateTime getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(LocalDateTime timeStamp) {
		this.timeStamp = timeStamp;
	}
	
	

}
