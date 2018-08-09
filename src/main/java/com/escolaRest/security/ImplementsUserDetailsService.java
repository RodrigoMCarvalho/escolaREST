package com.escolaRest.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.escolaRest.dao.UsuarioDAO;
import com.escolaRest.model.Usuario;

public class ImplementsUserDetailsService implements UserDetailsService {
	
	@Autowired
	private UsuarioDAO dao;

	@Override
	public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
		
			Usuario usuario = dao.findByLogin(login);

			if (usuario == null) {
				throw new UsernameNotFoundException("Usuario não encontrado!");
			}
			return new User(usuario.getUsername(), usuario.getPassword(), 
					true, true, true, true, usuario.getAuthorities());
	}
}
