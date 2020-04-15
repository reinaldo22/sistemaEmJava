package com.crud.spring.security;

import java.io.IOException;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.crud.spring.ApplicationContextLoad;
import com.crud.spring.model.Usuario;
import com.crud.spring.repository.UsuarioRepository;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
@Component
public class JWTTokenAutenticacaoService {
	/* Tempo de validade do TOKEN "30 MINUTOS" */
	private static final long EXPIRATION_TIME = 72000000;

	/* Senha unica para compor a autenticação e ajudar na segurança */
	private static final String SECRET = "=jbou3634632143543213535435143213543543543214=34354=3868=";

	/* TOKEN PREFIXO Padrão de autenticação */
	private static final String TOKEN_PREFIX = "Bearer";

	/* Cabeçalho da pagina de acesso */
	private static final String HEADER_STRING = "Authorization";

	/*
	 * Gerando TOKEN de autenticação e adicionando ao cabeçalho e resposta ao HTTP
	 */
	public void addAuthentication(HttpServletResponse response, String username) throws IOException {

		/* MONTAGEM DO TOKEN */

		String JWT = Jwts.builder() /* Chama o gerador de token */
				.setSubject(username)/* Chama o usuario */
				.setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) /* Tempo de EXPIRAÇÃO */
				.signWith(SignatureAlgorithm.HS256, SECRET).compact();/* Compactação e algoritmo de geração do token */

		/* Junta o token com o prefixo */
		String token = TOKEN_PREFIX + " " + JWT; /* Bearer */

		/* Adiciona no cabeçalho HTTP */
		response.addHeader(HEADER_STRING, token);/* Authorization: Bearer 7878787878 */

		liberacaoCors(response);

		/* Escreve token como resposta no corpo do HTTP */
		response.getWriter().write("{\"Authorization\": \"" + token + "\"}");

	}

	/* Retorna o usuário validado com o token ou não seja válido retorna nulo */
	public Authentication getAuthentication(HttpServletRequest request, HttpServletResponse response) {

		/* Pega o token enviado no cabeçalho http */
		String token = request.getHeader(HEADER_STRING);
		if (token != null) {

			/* Faz a validação do token do usuário na requisição */
			String user = Jwts.parser().setSigningKey(SECRET) /* Bearer 7878787878 */
					.parseClaimsJws(token.replace(TOKEN_PREFIX, ""))/* 7878787878 */
					.getBody().getSubject();

			if (user != null) {

				Usuario usuario = ApplicationContextLoad.getApplicationContext().getBean(UsuarioRepository.class)
						.findUserByLogin(user);

				System.out.println(">>>>>>" + usuario);

				if (usuario != null) { /* Vai retornar o usuário para validar por token */
					return new UsernamePasswordAuthenticationToken(usuario.getLogin(), usuario.getSenha(),
							usuario.getAuthorities());

				}
			}

		}
		/* Liberando resposta para porta diferente do projeto Angular */
		liberacaoCors(response);
		return null;
	}

	private void liberacaoCors(HttpServletResponse response) {
		if (response.getHeader("Access-Control-Allow-Origin") == null) {
			response.addHeader("Access-Control-Allow-Origin", "*");
		}

		if (response.getHeader("Access-Control-Allow-Headers") == null) {
			response.addHeader("Access-Control-Allow-Headers", "*");
		}
		if (response.getHeader("Access-Control-Request-Headers") == null) {
			response.addHeader("Access-Control-Request-Headers", "*");
		}
	}

}