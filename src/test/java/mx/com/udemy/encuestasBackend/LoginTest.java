package mx.com.udemy.encuestasBackend;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
import org.springframework.test.context.ActiveProfiles;

import mx.com.udemy.encuestasBackend.dto.UsuarioLoginDTO;
import mx.com.udemy.encuestasBackend.dto.UsuarioRegistroDTO;
import mx.com.udemy.encuestasBackend.entities.Usuario;
import mx.com.udemy.encuestasBackend.repository.IUsuarioRepository;
import mx.com.udemy.encuestasBackend.response.UsuarioRespuesta;
import mx.com.udemy.encuestasBackend.services.IUserService;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
//@ActiveProfiles("test")
public class LoginTest {

	// == end point
	private static final String API_LOGIN_URL = "/users/login";

	@Autowired
	private TestRestTemplate testTestTemplate; // sirve para enviar la peticion

	@Autowired
	private IUserService usuarioService;

	@Autowired
	private IUsuarioRepository userRepository;

	/**
	 * @BeforeEach se ejecuta antes de iniciar los test, en este caso borrara los
	 *             datos de la tabla usuario
	 */
	/*
	 * @BeforeEach public void cleanup() { userRepository.deleteAll(); }
	 */

	@Test
	public void postLogin_sinCredenciales_retornaForbidden() {
		ResponseEntity<Object> response = login(null, Object.class);
		assertEquals(response.getStatusCode(), HttpStatus.FORBIDDEN);
	}

	// Unathorizacion
	@Test
	public void postLogin_conCredenciales_IncorrectasForbidden() {
		UsuarioRegistroDTO usuarioRegistroDTO = TestUtil.crearUsuarioRegistroDTO();
		usuarioService.crearUsuario(usuarioRegistroDTO);
		UsuarioLoginDTO usuarioLoginDTO = new UsuarioLoginDTO();
		usuarioLoginDTO.setEmail("aaa@gamil.com");
		usuarioLoginDTO.setPassword("1234");
		ResponseEntity<Object> response = login(usuarioLoginDTO, Object.class);
		assertEquals(response.getStatusCode(), HttpStatus.FORBIDDEN);
	}

	@Test
	public void postLogin_conCredenciales_CorrectasOk() {
		UsuarioRegistroDTO usuarioRegistroDTO = TestUtil.crearUsuarioRegistroDTO();
		usuarioService.crearUsuario(usuarioRegistroDTO);
		UsuarioLoginDTO usuarioLoginDTO = new UsuarioLoginDTO();
		usuarioLoginDTO.setEmail(usuarioRegistroDTO.getEmail());
		usuarioLoginDTO.setPassword(usuarioRegistroDTO.getPassword());
		ResponseEntity<Object> response = login(usuarioLoginDTO, Object.class);
		assertEquals(response.getStatusCode(), HttpStatus.OK);
	}

	@Test
	public void postLogin_conCredenciales_CorrectasAuthToken() {
		UsuarioRegistroDTO usuarioRegistroDTO = TestUtil.crearUsuarioRegistroDTO();
		usuarioService.crearUsuario(usuarioRegistroDTO);
		UsuarioLoginDTO usuarioLoginDTO = new UsuarioLoginDTO();
		usuarioLoginDTO.setEmail(usuarioRegistroDTO.getEmail());
		usuarioLoginDTO.setPassword(usuarioRegistroDTO.getPassword());
		ResponseEntity<Map<String, String>> response = login(usuarioLoginDTO,
				new ParameterizedTypeReference<Map<String, String>>() {
				});
		Map<String, String>body=response.getBody();
		String token= body.get("token");
		assertTrue(token.contains("Bearer"));
	}

	/**
	 * Metodo para validar login
	 * @param <T>
	 * @param data
	 * @param responseType
	 * @return
	 */
	public <T> ResponseEntity<T> login(UsuarioLoginDTO data, Class<T> responseType) {
		return testTestTemplate.postForEntity(API_LOGIN_URL, data, responseType);
	}

	/**
	 * Metodo para validar el token
	 * 
	 * @param <T>
	 * @param data
	 * @param responseType
	 * @return
	 */
	public <T> ResponseEntity<T> login(UsuarioLoginDTO data, ParameterizedTypeReference<T> responseType) {
		HttpEntity<UsuarioLoginDTO> entity = new HttpEntity<UsuarioLoginDTO>(data, new HttpHeaders());
		return testTestTemplate.exchange(API_LOGIN_URL, HttpMethod.POST, entity, responseType);
	}
}
