package mx.com.udemy.encuestasBackend.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import mx.com.udemy.encuestasBackend.dto.UsuarioRegistroDTO;
import mx.com.udemy.encuestasBackend.entities.Usuario;
import mx.com.udemy.encuestasBackend.response.UsuarioRespuesta;
import mx.com.udemy.encuestasBackend.services.IUserService;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/users")
public class UserController {

	@Autowired
	private IUserService userService;
	
	private static final Logger log = LoggerFactory.getLogger(UserController.class);

	
    @PostMapping
    public ResponseEntity<UsuarioRespuesta> createUser(@Valid @RequestBody UsuarioRegistroDTO usuarioRegistroDTO) {
    	UsuarioRespuesta usuarioRespuesta= new UsuarioRespuesta();
    	BeanUtils.copyProperties(userService.crearUsuario(usuarioRegistroDTO), usuarioRespuesta);
        return new ResponseEntity<UsuarioRespuesta>( usuarioRespuesta,HttpStatus.CREATED);
    }
    
    @GetMapping
    public ResponseEntity<UsuarioRespuesta>obtenerUsuario(Authentication authentication){
    	Usuario usuario=userService.obtenerUsuario(authentication.getPrincipal().toString());
    	UsuarioRespuesta usuarioRespuesta= new UsuarioRespuesta();
    	BeanUtils.copyProperties(usuario, usuarioRespuesta);
    	return new ResponseEntity<UsuarioRespuesta>(usuarioRespuesta,HttpStatus.OK);
    	
    }
    
	
}
