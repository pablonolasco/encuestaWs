package mx.com.udemy.encuestasBackend.services.impl;

import java.util.ArrayList;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import mx.com.udemy.encuestasBackend.dto.UsuarioRegistroDTO;
import mx.com.udemy.encuestasBackend.entities.Usuario;
import mx.com.udemy.encuestasBackend.repository.IUsuarioRepository;
import mx.com.udemy.encuestasBackend.services.IUserService;

@Service
public class UserServiceImpl implements IUserService {

	private IUsuarioRepository usuarioRepository;
	
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	public UserServiceImpl(IUsuarioRepository usuarioRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
		this.usuarioRepository = usuarioRepository;
		this.bCryptPasswordEncoder=bCryptPasswordEncoder;
	}

	@Override
	public Usuario crearUsuario(UsuarioRegistroDTO usuarioDTO) {
		Usuario usuario= new Usuario();
		// copia el valor de las propiedades de un objeto a otro
		BeanUtils.copyProperties(usuarioDTO, usuario);
		// en caso de que el nombre de la variable del dto sea distinta a la del modelo es asi como se puede guardar el valor del request
		usuario.setPassword(bCryptPasswordEncoder.encode(usuarioDTO.getPassword()));
		return usuarioRepository.save(usuario);
	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Usuario usuario=usuarioRepository.findByEmail(email);
		if (usuario == null) {
			throw new UsernameNotFoundException(email);
		} 
		
		return new User(usuario.getEmail(), usuario.getPassword(), new ArrayList<>());
	}

	@Override
	public Usuario obtenerUsuario(String email) {
		return usuarioRepository.findByEmail(email);
	}

}
