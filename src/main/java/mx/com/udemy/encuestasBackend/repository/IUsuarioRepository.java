package mx.com.udemy.encuestasBackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import mx.com.udemy.encuestasBackend.entities.Usuario;

@Repository
public interface IUsuarioRepository extends JpaRepository<Usuario, Long> {

	Usuario findByEmail(String email);

}
