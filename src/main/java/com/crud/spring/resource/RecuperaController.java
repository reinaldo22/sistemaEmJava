package com.crud.spring.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.crud.spring.ObjetoError;
import com.crud.spring.model.Usuario;
import com.crud.spring.repository.UsuarioRepository;

@RestController
@RequestMapping("/recuperar")
public class RecuperaController {

	@Autowired
	private UsuarioRepository usuarioRepo;

	@ResponseBody
	@PostMapping(value = "/")
	public ResponseEntity<ObjetoError> recuperar(@RequestBody Usuario login) {
		ObjetoError objetoError = new ObjetoError();

		Usuario user = usuarioRepo.findUserByLogin(login.getLogin());

		if (user == null) {
			objetoError.setCode("404");
			objetoError.setError("Usuário não encontrado");
		} else {
			objetoError.setCode("200");
			objetoError.setError("Acesso enciado para o seu e-mail");
		}
		return new ResponseEntity<ObjetoError>(objetoError, HttpStatus.OK);
	}

}
