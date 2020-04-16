package com.crud.spring.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.crud.spring.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

	@Query("SELECT u FROM Usuario u WHERE u.login = ?1")
	Usuario findUserByLogin(String username);
	
	@Query("SELECT u FROM Usuario u WHERE u.nome like %?1%")
	List<Usuario> findUserByNome(String username);
}
