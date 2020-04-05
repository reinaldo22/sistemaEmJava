package com.crud.spring.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.AntPathMatcher;

import com.crud.spring.services.DetailServicesImpl;

/*Mapeia url, enderecos, autoriza ou bloqueia acessos ´para a url*/

@Configuration
@EnableWebSecurity
public class WebConfigSecurity extends WebSecurityConfigurerAdapter {

	@Autowired
	private DetailServicesImpl detailservice;

	/* Configura as solicitacoes de acesso por http */
	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http.csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())

				/* ativando restricoes url */
				.disable().authorizeRequests().antMatchers("/").permitAll().antMatchers("index").permitAll()
				/* URL de logout - Redirciona após o usuario deslogar do sistema */
				.anyRequest().authenticated().and().logout().logoutSuccessUrl("/index")
				/* Mapeia URL de Logout e invalida o usuario */
				.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))

				/* Filtra requisicoes de login para autenticacao */
				.and()
				.addFilterBefore(new JWTLoginFilter("/login", authenticationManager()),
						UsernamePasswordAuthenticationFilter.class)
				/*
				 * Filtra demais requisicoes para verificar a presenca do token JWT no header
				 * http
				 */
				.addFilterBefore(new JwtApiAutenticacaoFilter(), UsernamePasswordAuthenticationFilter.class);
	}

	/* irá consultar o usuario no bando de dados */
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(detailservice).passwordEncoder(new BCryptPasswordEncoder());
	}
}
