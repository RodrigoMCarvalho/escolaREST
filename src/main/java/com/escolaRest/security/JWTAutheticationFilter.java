package com.escolaRest.security;

import java.io.IOException;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.escolaRest.model.Usuario;
import static com.escolaRest.security.SecurityConstants.*;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class JWTAutheticationFilter extends UsernamePasswordAuthenticationFilter{
	
	private AuthenticationManager authenticationManager;
	
	public JWTAutheticationFilter(AuthenticationManager authenticationManager ) {
		this.authenticationManager = authenticationManager;
	}
	
	@Override  //realizada a tentativa de autenticação, se bem sucedida, o Spring passar para o próximo método
	public Authentication attemptAuthentication(HttpServletRequest request, 
												HttpServletResponse response)
			throws AuthenticationException {
		try {
			Usuario usuario = new ObjectMapper().readValue(request.getInputStream(), Usuario.class);
			return this.authenticationManager
					.authenticate(new UsernamePasswordAuthenticationToken(usuario.getLogin(), usuario.getSenha()));
		} catch (IOException e) {
			throw new RuntimeException(e);	
		}
	}
	
	@Override
	protected void successfulAuthentication(HttpServletRequest request, 
											HttpServletResponse response, 
											FilterChain chain,
		Authentication authResult) throws IOException, ServletException {
		String username = ((User) authResult.getPrincipal()).getUsername();
		String token = Jwts
				.builder()
				.setSubject(username)
				.setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) //importados estaticamentes
				.signWith(SignatureAlgorithm.HS512, SECRET)
				.compact();
		
		String bearerToken = TOKEN_PREFIX + token;
		response.getWriter().write(bearerToken); //adicionar o token no body
		response.addHeader( HEADER_STRING, bearerToken);
	}
}
