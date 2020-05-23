package com.crud.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.crud.spring.model.Profissao;


public interface ProfissaoRepository extends JpaRepository<Profissao, Long> {

}
