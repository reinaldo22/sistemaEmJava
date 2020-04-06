package com.crud.spring.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.crud.spring.model.Usuario;
import com.crud.spring.repository.UsuarioRepository;

@Service
public class DetailServicesImpl implements UserDetailsService{
	@Autowired
	private UsuarioRepository userRepo;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		Usuario usuario = userRepo.findUserByLogin(username);

		if (usuario == null) {
			throw new UsernameNotFoundException("Usuário não foi encontrador!!!");
		}

		return new User(usuario.getLogin(),
				usuario.getPassword(),
				usuario.getAuthorities());
	}
}
