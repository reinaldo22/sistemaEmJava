package com.crud.spring.resource;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.crud.spring.ObjetoError;
import com.crud.spring.model.Usuario;
import com.crud.spring.repository.UsuarioRepository;
import com.crud.spring.service.EmailService;

@RestController
@RequestMapping("/recuperar")
public class RecuperaController {

	@Autowired
	private UsuarioRepository usuarioRepo;

	@Autowired
	private EmailService emailservice;
	
	@ResponseBody
	@PostMapping(value = "/")
	public ResponseEntity<ObjetoError> recuperar(@RequestBody Usuario login) throws Exception {
		ObjetoError objetoError = new ObjetoError();

		Usuario user = usuarioRepo.findUserByLogin(login.getLogin());

		if (user == null) {
			objetoError.setCode("404");
			objetoError.setError("Usuário não encontrado");
		} else {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			String senhaNova = dateFormat.format(Calendar.getInstance().getTime());
			String senhaNovaCriptografada = new BCryptPasswordEncoder().encode(senhaNova);
			
			usuarioRepo.updateSenha(senhaNovaCriptografada, user.getId());
			
			emailservice.enviarEmail("Recuperação se senha", user.getLogin(),
					"Sua nova senha é: " + senhaNova);
			
			objetoError.setCode("200");
			objetoError.setError("Acesso enviado para o seu e-mail");
		}
		return new ResponseEntity<>(objetoError, HttpStatus.OK);
	}

}
