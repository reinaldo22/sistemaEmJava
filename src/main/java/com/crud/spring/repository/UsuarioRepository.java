package com.crud.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;


import com.crud.spring.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

	
	Usuario findUserByLogin(String login);
}
