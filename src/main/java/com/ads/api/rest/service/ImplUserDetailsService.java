package com.ads.api.rest.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ads.api.rest.model.Usuario;
import com.ads.api.rest.repository.UsuarioRepository;

@Service
public class ImplUserDetailsService implements UserDetailsService {
	
	@Autowired
	private UsuarioRepository userRepo;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		/*Consultar no banco o usuario*/
		
		Usuario user = userRepo.findUserByLogin(username);
		
		if (user == null) {
			throw new UsernameNotFoundException("Usuario não foi encontrado");
		}
		
		return new User(user.getLogin(),
				user.getPassword(),
				user.getAuthorities());
	}

}
