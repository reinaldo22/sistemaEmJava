package com.crud.spring.resource;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.crud.spring.model.Profissao;
import com.crud.spring.repository.ProfissaoRepository;

@RestController
@RequestMapping("/profissao")
public class ProfissaoController {

	@Autowired
	ProfissaoRepository profissaorepo;

	@GetMapping(value = "/", produces = "application/json")
	private ResponseEntity<List<Profissao>> profissoes() {

		List<Profissao> listaProfissao = profissaorepo.findAll();

		return new ResponseEntity<List<Profissao>>(listaProfissao, HttpStatus.OK);
	}

}
