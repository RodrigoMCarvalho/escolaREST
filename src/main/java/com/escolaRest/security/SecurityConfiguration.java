package com.escolaRest.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled=true) //devido ao @PreAuthorize em EstudanteResource
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private ImplementsUserDetailsService userDetailsService;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
//		http.authorizeRequests()
//			.antMatchers("/*/protected/**").hasRole("USER")
//			.antMatchers("/*/admin/**").hasRole("ADMIN")
//			.and()
//			.httpBasic()
//			.and()
//			.csrf().disable();
		
		// autenticação com JWT
		http.cors().configurationSource(request -> new CorsConfiguration().applyPermitDefaultValues())
			.and().csrf().disable().authorizeRequests()
			.antMatchers(HttpMethod.GET, SecurityConstants.SIGN_UP_URL).permitAll()
			.antMatchers("/*/protected/**").hasRole("USER")
			.antMatchers("/*/protected/**").hasRole("ADMIN")
			.antMatchers("/*/admin/**").hasRole("ADMIN")
			.and()
			.addFilter(new JWTAutheticationFilter(authenticationManager()))
			.addFilter(new JWTAuthorizationFilter(authenticationManager(), userDetailsService));
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService)
			.passwordEncoder(new BCryptPasswordEncoder());
		
//		auth.inMemoryAuthentication()
//			.withUser("rodrigo").password("{noop}123").roles("ADMIN", "USER") //autenticação em memória
//			.and()
//			.withUser("gugu").password("{noop}123").roles("USER");
	}

}
