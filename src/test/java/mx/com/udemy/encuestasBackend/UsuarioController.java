package mx.com.udemy.encuestasBackend;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Map;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import mx.com.udemy.encuestasBackend.dto.UsuarioLoginDTO;
import mx.com.udemy.encuestasBackend.dto.UsuarioRegistroDTO;
import mx.com.udemy.encuestasBackend.entities.Usuario;
import mx.com.udemy.encuestasBackend.exception.ValidationErrors;
import mx.com.udemy.encuestasBackend.repository.IUsuarioRepository;
import mx.com.udemy.encuestasBackend.response.UsuarioRespuesta;
import mx.com.udemy.encuestasBackend.services.IUserService;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class UsuarioController {

	private static final String API_URL = "/users";
	private static final String API_LOGIN_URL = "/users/login";
	
	private static final Logger log = LoggerFactory.getLogger(UsuarioController.class);


	@Autowired
	private TestRestTemplate testTestTemplate; // sirve para enviar la peticion

	@Autowired
	private IUserService userService;

	@Autowired
	private IUsuarioRepository usuarioRepository;

	@Test
	public void createUser_sinElCampoNombre_retornaBadRequest() {
		UsuarioRegistroDTO usuarioRegistroDTO = new UsuarioRegistroDTO();
		usuarioRegistroDTO.setNombre(null);
		ResponseEntity<Object> response = register(usuarioRegistroDTO, Object.class);
		assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
	}

	@Test
	public void createUser_sinDatos_retornaBadRequest() {
		ResponseEntity<Object> response = register(new UsuarioRegistroDTO(), Object.class);
		assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
	}

	@Test
	public void createUser_sinElCampoPassword_retornaBadRequest() {
		UsuarioRegistroDTO usuarioRegistroDTO = new UsuarioRegistroDTO();
		usuarioRegistroDTO.setPassword(null);
		ResponseEntity<Object> response = register(usuarioRegistroDTO, Object.class);
		assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
	}

	@Test
	public void createUser_sinElCampoEmail_retornaBadRequest() {
		UsuarioRegistroDTO usuarioRegistroDTO = new UsuarioRegistroDTO();
		usuarioRegistroDTO.setEmail(null);
		ResponseEntity<Object> response = register(usuarioRegistroDTO, Object.class);
		assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
	}

	@Test
	public void createUser_sinDatos_retornaMensajeErroresValidacion() {
		ResponseEntity<ValidationErrors> response = register(new UsuarioRegistroDTO(), ValidationErrors.class);
		Map<String, String> errores = response.getBody().getErrors();
		assertEquals(errores.size(), 3);
	}

	@Test
	public void createUser_sinDatos_retornaMensajeErroresValidacionNombre() {
		UsuarioRegistroDTO usuarioRegistroDTO = new UsuarioRegistroDTO();
		usuarioRegistroDTO.setNombre(null);
		ResponseEntity<ValidationErrors> response = register(usuarioRegistroDTO, ValidationErrors.class);
		Map<String, String> errores = response.getBody().getErrors();
		assertTrue(errores.containsKey("nombre"));
	}

	@Test
	public void createUser_sinDatos_retornaMensajeErroresValidacionEmail() {
		UsuarioRegistroDTO usuarioRegistroDTO = new UsuarioRegistroDTO();
		usuarioRegistroDTO.setEmail(null);
		ResponseEntity<ValidationErrors> response = register(usuarioRegistroDTO, ValidationErrors.class);
		Map<String, String> errores = response.getBody().getErrors();
		assertTrue(errores.containsKey("email"));
	}

	@Test
	public void createUser_sinDatos_retornaMensajeErroresValidacionPassword() {
		UsuarioRegistroDTO usuarioRegistroDTO = new UsuarioRegistroDTO();
		usuarioRegistroDTO.setPassword(null);
		ResponseEntity<ValidationErrors> response = register(usuarioRegistroDTO, ValidationErrors.class);
		Map<String, String> errores = response.getBody().getErrors();
		assertTrue(errores.containsKey("password"));
	}

	@Test
	public void createUser_conUsuarioValido_retornaOK() {
		UsuarioRegistroDTO user = TestUtil.crearUsuarioRegistroDTO();
		ResponseEntity<UsuarioRespuesta> response = register(user, UsuarioRespuesta.class);
		assertEquals(response.getStatusCode(), HttpStatus.CREATED);
	}
	
	@Test
	public void createUser_conUsuarioValido_retornaUsuarioResponse() {
		UsuarioRegistroDTO user = TestUtil.crearUsuarioRegistroDTO();
		ResponseEntity<UsuarioRespuesta> response = register(user, UsuarioRespuesta.class);
		assertEquals(response.getBody().getNombre(), user.getNombre());
	}
	
	@Test
	public void create_conUsuario_Valido_guardar_en_bd() {
		UsuarioRegistroDTO usuarioRegistroDTO=TestUtil.crearUsuarioRegistroDTO();
		ResponseEntity<UsuarioRespuesta>responseEntity=register(usuarioRegistroDTO, UsuarioRespuesta.class);
		Usuario usuario=usuarioRepository.findById(responseEntity.getBody().getId())
				.orElseThrow(null);
		assertNotNull(usuario);
	}
	
	@Test
	public void create_conUsuario_Valido_guardar_passwordConHash_en_bd() {
		UsuarioRegistroDTO usuarioRegistroDTO=TestUtil.crearUsuarioRegistroDTO();
		ResponseEntity<UsuarioRespuesta>responseEntity=register(usuarioRegistroDTO, UsuarioRespuesta.class);
		Usuario usuario=usuarioRepository.findById(responseEntity.getBody().getId())
				.orElseThrow(null);
		assertNotEquals(usuarioRegistroDTO.getPassword(), usuario.getPassword());
	}
	
	@Test
	public void create_conUsuario_Valido_guardar_correoExiste_en_bd() {
		UsuarioRegistroDTO usuarioRegistroDTO=TestUtil.crearUsuarioRegistroDTO();
		register(usuarioRegistroDTO, UsuarioRespuesta.class);
		ResponseEntity<UsuarioRespuesta>responseEntity=register(usuarioRegistroDTO, UsuarioRespuesta.class);
		
		assertEquals(responseEntity.getStatusCode(), HttpStatus.BAD_REQUEST);
	}
	
	@Test
	public void createUser_sinDatos_retornaMensajeErroresValidacionEmailExisteEnBaseDatos() {
		UsuarioRegistroDTO usuarioRegistroDTO=TestUtil.crearUsuarioRegistroDTO();
		register(usuarioRegistroDTO, UsuarioRespuesta.class);
		ResponseEntity<ValidationErrors> response = register(usuarioRegistroDTO, ValidationErrors.class);
		Map<String, String> errores = response.getBody().getErrors();
		assertTrue(errores.containsKey("email"));
	}
	
	@Test
	public void getUser_sinToken_Autenticacion_retornaForbidden() {
		ResponseEntity<Object>responseEntity=login(null, new ParameterizedTypeReference<Object>() {
		});
		assertEquals(responseEntity.getStatusCode(), HttpStatus.FORBIDDEN);
	}
	
	@Test
	public void getUsuario_conToken_Autenthicacion_retornaUsuarioRespuestaOK() {
		UsuarioRegistroDTO usuarioRegistroDTO=TestUtil.crearUsuarioRegistroDTO();
		userService.crearUsuario(usuarioRegistroDTO);
		UsuarioLoginDTO usuario=new UsuarioLoginDTO();
		usuario.setEmail(usuarioRegistroDTO.getEmail());
		usuario.setPassword(usuarioRegistroDTO.getPassword());
		ResponseEntity<Map<String, String>>responseEntity= login(usuario, 
				new ParameterizedTypeReference<Map<String,String>>() {});
		Map<String, String>body=responseEntity.getBody();
		String token= body.get("token").replace("Bearer", "");
		
		ResponseEntity<UsuarioRespuesta>response= getUsuario(token, new ParameterizedTypeReference<UsuarioRespuesta>() {
		});
		
		assertEquals(response.getStatusCode(), HttpStatus.OK);
	}
	
	@Test
	public void getUsuario_conToken_Autenthicacion_retornaUsuarioRespuesta() {
		UsuarioRegistroDTO usuarioRegistroDTO=TestUtil.crearUsuarioRegistroDTO();
		userService.crearUsuario(usuarioRegistroDTO);
		UsuarioLoginDTO usuario=new UsuarioLoginDTO();
		usuario.setEmail(usuarioRegistroDTO.getEmail());
		usuario.setPassword(usuarioRegistroDTO.getPassword());
		ResponseEntity<Map<String, String>>responseEntity= login(usuario, 
				new ParameterizedTypeReference<Map<String,String>>() {});
		Map<String, String>body=responseEntity.getBody();
		String token= body.get("token").replace("Bearer", "");
		
		ResponseEntity<UsuarioRespuesta>response= getUsuario(token, new ParameterizedTypeReference<UsuarioRespuesta>() {
		});
		
		assertEquals(usuarioRegistroDTO.getNombre(), response.getBody().getNombre());
	}
	
	/**
	 * Metodo para enviar la peticion a la url users 
	 * @param <T> the type of the return value
	 * @param token
	 * @param responseType the type of the return value
	 * @return the response as entity
	 */
	public <T> ResponseEntity<T> getUsuario(String token, ParameterizedTypeReference<T> responseType) {
		HttpHeaders headers= new HttpHeaders();
		headers.setBearerAuth(token);
		HttpEntity<Object> entity = new HttpEntity<Object>(null, headers);
		return testTestTemplate.exchange(API_URL, HttpMethod.GET, entity, responseType);
	}

	/**
	 * Metodo para enviar la peticion a la url login 
	 * Execute the HTTP method to the given URI template, writing the given request entity
	 * to the request, and returns the response as {@link ResponseEntity}.
	 * @param <T> the type of the return value
	 * @param usuarioLoginDTO {@link UsuarioLoginDTO} the entity (headers and/or body) to write to the request, may
	 * be {@code null}
	 * @param responseType the type of the return value
	 * @return the response as entity
	 */
	public <T> ResponseEntity<T> login(UsuarioLoginDTO usuarioLoginDTO, ParameterizedTypeReference<T> responseType) {
		HttpEntity<Object> entity = new HttpEntity<Object>(usuarioLoginDTO, new HttpHeaders());
		return testTestTemplate.exchange(API_LOGIN_URL, HttpMethod.POST, entity, responseType);
	}

	/**
	 * Metodo para enviar peticion a url login para su registro
	 * Create a new resource by POSTing the given object to the URI template, and returns
	 * the response as {@link ResponseEntity}.
	 * @param <T>
	 * @param data {@link UsuarioRegistroDTO} request the Object to be POSTed, may be {@code null}
	 * @param responseType {@link responseType} the type of the return value
	 * @return testTestTemplate 
	 */
	public <T> ResponseEntity<T> register(UsuarioRegistroDTO data, Class<T> responseType) {
		return testTestTemplate.postForEntity(API_URL, data, responseType);
	}

}
