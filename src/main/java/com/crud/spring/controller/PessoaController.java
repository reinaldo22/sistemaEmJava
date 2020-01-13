package com.crud.spring.controller;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.crud.spring.model.Pessoa;
import com.crud.spring.services.PessoaService;

@Controller
@RequestMapping
public class PessoaController {

	@Autowired
	private PessoaService pservice;

	@GetMapping("/listar")
	public String listar(Model model) {
		List<Pessoa> pessoas = pservice.listar();
		model.addAttribute("pessoas", pessoas);

		return "index";
	}

	@GetMapping("/new")
	public String salvar(Model model) {
		model.addAttribute("pessoa", new Pessoa());

		return "form";
	}

	@PostMapping("save")
	public String save(@Valid Pessoa p, Model model) {

		pservice.save(p);
		return "redirect:/listar";
	}
	
	@GetMapping("/editar/{id}")
	public String editar(@PathVariable int id, Model model) {
		
		Optional<Pessoa> pessoa = pservice.listarId(id);
		
		model.addAttribute("pessoa", pessoa );
		
		
		return "form";
	}
}
