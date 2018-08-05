package com.escolaRest.javaClient;

import java.util.List;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.escolaRest.model.Estudante;
import com.escolaRest.model.PageableResponse;

public class JavaClientDAO {

	private RestTemplate restTemplate = new RestTemplateBuilder()
			.rootUri("http://localhost:8080/v1/protected/escolaRest").basicAuthorization("gugu", "123").build();

	private RestTemplate restTemplateAdmin = new RestTemplateBuilder()
			.rootUri("http://localhost:8080/v1/admin/escolaRest").basicAuthorization("rodrigo", "123").build();

	public Estudante findById(Long id) {

		return restTemplate.getForObject("/{id}", Estudante.class, id); // ID = 1
		// ResponseEntity<Estudante> forEntity = restTemplateAdmin
		// .getForEntity("/", Estudante.class , 1);
	}

	public List<Estudante> listAll() {
		ResponseEntity<PageableResponse<Estudante>> exchange = restTemplate.exchange("/", HttpMethod.GET, null,
				new ParameterizedTypeReference<PageableResponse<Estudante>>() {
				});
		return exchange.getBody().getContent();
		
	}

	public Estudante save(Estudante estudante) {
		ResponseEntity<Estudante> exchangePost = restTemplateAdmin.exchange("/", HttpMethod.POST,
				new HttpEntity<>(estudante, createJsonHeader()), Estudante.class);

		// Estudante estudantePostForObjet = restTemplateAdmin.postForObject("/",
		// estudantePost, Estudante.class);
		// ResponseEntity<Estudante> estudanteEsponseEntity =
		// restTemplateAdmin.postForEntity("/", estudantePost, Estudante.class);
		// === 3 maneiras de usar POST

		return exchangePost.getBody();
	}
	
	public void update(Estudante estudante) {
		restTemplateAdmin.put("/", estudante);
	}
	
	public void delete(Estudante estudante) {
		restTemplateAdmin.delete("/{id}", estudante);
	}

	private static HttpHeaders createJsonHeader() {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		return headers;
	}

}
