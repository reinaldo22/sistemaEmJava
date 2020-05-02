package com.crud.spring.repository;


import org.springframework.data.jpa.repository.JpaRepository;


import com.crud.spring.model.Telefone;


public interface TelefoneRepository extends JpaRepository<Telefone, Long> {

	
}
