package com.crud.spring.resource;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.crud.spring.model.Usuario;
import com.crud.spring.repository.UsuarioRepository;


@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/usuario")
public class IndexController {

	@Autowired
	private UsuarioRepository usuarioRepository;

	@GetMapping(value = "/{id}", produces = "application/json")
	public ResponseEntity<Usuario> init(@PathVariable(value = "id") Long id) {

		Optional<Usuario> usuario = usuarioRepository.findById(id);

		return new ResponseEntity<Usuario>(usuario.get(), HttpStatus.OK);
	}
	
	@GetMapping(value = "/", produces = "application/json")
	@Cacheable("cacheusuarios")
	public ResponseEntity<List<Usuario>> get() throws InterruptedException {

		List<Usuario> list = (List<Usuario>) usuarioRepository.findAll();

		Thread.sleep(6000);
		
		return new ResponseEntity<List<Usuario>>(list, HttpStatus.OK);

	}

	@PostMapping(value = "/", produces = "application/json")
	public ResponseEntity<Usuario> cadastrar(@RequestBody Usuario usuario) {

		for (int i = 0; i < usuario.getTelefones().size(); i++) {
			usuario.getTelefones().get(i).setUsuarios(usuario);
		}

		Usuario dbUser = usuarioRepository.save(usuario);

		return new ResponseEntity<Usuario>(dbUser, HttpStatus.OK);

	}

	@PutMapping(value = "/", produces = "application/json")
	public ResponseEntity<Usuario> atualizar(@RequestBody Usuario usuario) {

		for (int i = 0; i < usuario.getTelefones().size(); i++) {
			usuario.getTelefones().get(i).setUsuarios(usuario);
		}
		Usuario usuarioSalvo = usuarioRepository.save(usuario);
		return new ResponseEntity<Usuario>(usuarioSalvo, HttpStatus.OK);
	}

}
