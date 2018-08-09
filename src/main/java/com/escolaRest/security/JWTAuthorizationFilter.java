package com.escolaRest.security;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

public class JWTAuthorizationFilter extends BasicAuthenticationFilter{

	public JWTAuthorizationFilter(AuthenticationManager authenticationManager) {
		super(authenticationManager);
		// TODO Auto-generated constructor stub
	}
	
	
}
