package com.crud.spring.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.crud.spring.model.Pessoa;
import com.crud.spring.repository.PessoaRepository;


@Service
public class PessoaService{
	
	@Autowired
	private PessoaRepository dataRepository;
	

	
	public int save(Pessoa p) {
		int res = 0;
		Pessoa pessoa = dataRepository.save(p);
		if(!pessoa.equals(null)) {
			res =1;
		}
		return res;
	}

	
	public List<Pessoa> listar() {
		
		return dataRepository.findAll();
	}

	
	public Optional<Pessoa> listarId(int id) {
		
		return dataRepository.findById(id);
	}

	
	public void delete(int id) {
		dataRepository.deleteById(id);
		
		
	}
	
	

}
