package com.escolaRest.security;

import java.util.concurrent.TimeUnit;

public class SecurityConstants {
	static final String SECRET = "segredo";
	static final String TOKEN_PREFIX = "Bearer ";
	static final String HEADER_STRING = "Authorization";
	static final String SIGN_UP_URL = "/usuarios/sign-up";
	static final long EXPIRATION_TIME = 86400000L;
	
	public static void main(String[] args) {
		System.out.println(TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS)); //86400000
		//para descobrir quanto milisegundos tem 1 dia, visto que essa será a validade do token
	}
}
