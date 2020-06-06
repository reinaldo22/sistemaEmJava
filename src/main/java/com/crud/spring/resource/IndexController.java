package com.crud.spring.resource;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.crud.spring.model.Usuario;
import com.crud.spring.repository.TelefoneRepository;
import com.crud.spring.repository.UsuarioRepository;
import com.crud.spring.service.ImplementacaoUserDetailService;
import com.crud.spring.service.RelatorioService;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/usuario")
public class IndexController {

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private ImplementacaoUserDetailService userDetailService;

	@Autowired
	private TelefoneRepository telefoneRepo;

	@Autowired
	private RelatorioService relatorioService;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@GetMapping(value = "/{id}", produces = "application/json")
	@CachePut("cacheusuarios")
	public ResponseEntity<Usuario> init(@PathVariable(value = "id") Long id) {

		Optional<Usuario> usuario = usuarioRepository.findById(id);

		return new ResponseEntity<Usuario>(usuario.get(), HttpStatus.OK);
	}

	@GetMapping(value = "/", produces = "application/json")
	@CachePut("cacheusuarios")
	public ResponseEntity<Page<Usuario>> get() throws InterruptedException {

		PageRequest page = PageRequest.of(0, 5, Sort.by("nome"));
		Page<Usuario> list = usuarioRepository.findAll(page);

		return new ResponseEntity<Page<Usuario>>(list, HttpStatus.OK);

	}

	@GetMapping(value = "/page/{pagina}", produces = "application/json")
	@CachePut("cacheusuarios")
	public ResponseEntity<Page<Usuario>> usuarioPage(@PathVariable("pagina") int pagina) throws InterruptedException {

		PageRequest page = PageRequest.of(pagina, 5, Sort.by("nome"));
		Page<Usuario> list = usuarioRepository.findAll(page);

		return new ResponseEntity<Page<Usuario>>(list, HttpStatus.OK);

	}

	@GetMapping(value = "/buscaNome/{nome}", produces = "application/json")
	@CachePut("cacheusuarios")
	public ResponseEntity<Page<Usuario>> findNome(@PathVariable("nome") String nome) throws InterruptedException {

		PageRequest pageRequest = null;
		Page<Usuario> list = null;

		if (nome == null || (nome != null && nome.trim().isEmpty()) || nome.equalsIgnoreCase("undefined")) {
			pageRequest = PageRequest.of(0, 5, Sort.by("nome"));
			list = usuarioRepository.findAll(pageRequest);
		} else {
			pageRequest = PageRequest.of(0, 5, Sort.by("nome"));
			list = usuarioRepository.findUserByNamePage(nome, pageRequest);
		}

		return new ResponseEntity<Page<Usuario>>(list, HttpStatus.OK);

	}

	@PostMapping(value = "/", produces = "application/json")
	public ResponseEntity<Usuario> cadastrar(@RequestBody Usuario usuario) {

		// Caso o telefone não seja salvo automaticamente
		for (int i = 0; i < usuario.getTelefones().size(); i++) {
			usuario.getTelefones().get(i).setUsuarios(usuario);
		}

		String senhacriptografada = new BCryptPasswordEncoder().encode(usuario.getSenha());
		usuario.setSenha(senhacriptografada);
		Usuario usuarioSalvo = usuarioRepository.save(usuario);

		userDetailService.insereAcessoPadrao(usuarioSalvo.getId());
		return new ResponseEntity<Usuario>(usuarioSalvo, HttpStatus.OK);

	}

	@PutMapping(value = "/", produces = "application/json")
	public ResponseEntity<Usuario> atualizar(@RequestBody Usuario usuario) {

		for (int i = 0; i < usuario.getTelefones().size(); i++) {
			usuario.getTelefones().get(i).setUsuarios(usuario);
		}

		Usuario userTemporario = usuarioRepository.findById(usuario.getId()).get();
		if (!userTemporario.getSenha().equals(usuario.getSenha())) {/* senhas diferentes */
			String senhacriptografada = new BCryptPasswordEncoder().encode(usuario.getSenha());
			usuario.setSenha(senhacriptografada);
		}

		Usuario usuarioSalvo = usuarioRepository.save(usuario);
		return new ResponseEntity<Usuario>(usuarioSalvo, HttpStatus.OK);
	}

	@DeleteMapping(value = "/{id}", produces = "application/text")
	public String deletar(@PathVariable("id") Long id) {
		usuarioRepository.deleteById(id);

		return "Excluído com sucesso!";
	}

	@DeleteMapping(value = "/removeTelefone/{id}", produces = "application/text")
	public String deletaTelefone(@PathVariable("id") Long id) {

		telefoneRepo.deleteById(id);

		return "ok";
	}

	@GetMapping(value = "/relatorio", produces = "application/text")
	public ResponseEntity<String> donwloadRelatorio(HttpServletRequest request) throws Exception {

		byte[] pdf = relatorioService.gerarRelatorio("documento", request.getServletContext());

		String base64Pdf = "data:application/pdf;base64," + Base64.encodeBase64String(pdf);

		return new ResponseEntity<String>(base64Pdf, HttpStatus.OK);

	}

	@GetMapping(value = "/grafico", produces = "application/json")
	public ResponseEntity<UserChart> grafico() {
		UserChart userChat = new UserChart();

		List<String> resultado = jdbcTemplate.queryForList("select array_agg( nome ) from usuario where salario > 0 and nome <> '' union all select cast(array_agg(salario) as character varying[]) from usuario where salario > 0 and nome <> ''", String.class);

		if (!resultado.isEmpty()) {
			String nomes = resultado.get(0).replaceAll("\\{", "").replaceAll("\\}", "");
			String salario = resultado.get(1).replaceAll("\\{", "").replaceAll("\\}", "");

			userChat.setNome(nomes);
			userChat.setSalario(salario);
		}

		return new ResponseEntity<UserChart>(userChat, HttpStatus.OK);
	}

}
