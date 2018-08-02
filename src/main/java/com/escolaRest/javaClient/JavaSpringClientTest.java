package com.escolaRest.javaClient;

import java.util.Arrays;
import java.util.List;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.escolaRest.model.Estudante;

public class JavaSpringClientTest {
	public static void main(String[] args) {
		
		  RestTemplate restTemplate = new RestTemplateBuilder()
		            .rootUri("http://localhost:8080/v1/protected/escolaRest")
		            .basicAuthorization("gugu", "123")
		            .build();
		 Estudante estudante = restTemplate.getForObject("/{id}", Estudante.class, 1); // ID = 1
		 System.out.println(estudante);
		 
		 Estudante[] estudantes = restTemplate.getForObject("/", Estudante[].class); //sem paginação
		 System.out.println(Arrays.toString(estudantes));
		 
		 ResponseEntity<List<Estudante>> exchange = restTemplate.exchange("/", HttpMethod.GET, null, 
				 new ParameterizedTypeReference<List<Estudante>>() {
		 });
		 System.out.println(exchange.getBody());
	}
}
