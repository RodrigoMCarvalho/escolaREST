package com.escolaRest.resource;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.escolaRest.dao.EstudanteDAO;
import com.escolaRest.model.Estudante;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT) //utilizar uma porta aleatória a cada teste
@AutoConfigureMockMvc
public class EstudanteResourceTest {

	@Autowired
	private TestRestTemplate testRestTemplate;
	
	@Autowired
	private MockMvc mockMvc;  //pode subustituir o TestRestTemplate
	
	@LocalServerPort
	private int port;
	
	@MockBean
	private EstudanteDAO dao;
	
	@TestConfiguration   //Classe de configuração de testes
	static class Config {
		
		@Bean
		public RestTemplateBuilder restTemplateBuilder() {
			return new RestTemplateBuilder().basicAuthorization("rodrigo", "123");
		}
	}
	
	@Test
	public void listStudentsWhenUsernameAndPasswordAreIncorrectShouldReturnStatusCode401() {
		System.out.println("Porta utilizada: " + port);
		testRestTemplate = testRestTemplate.withBasicAuth("1", "1");
		ResponseEntity<String> response = testRestTemplate.getForEntity("/v1/protected/escolaRest", String.class);
		
		Assertions.assertThat(response.getStatusCodeValue()).isEqualTo(401);
	}
	
	@Before
	public void setup() {
		Estudante estudante = new Estudante(1L, "test01");
		Optional<Estudante> estudanteOpt = dao.findById(estudante.getId());
		
		BDDMockito.when(dao.findById(estudante.getId())).thenReturn(estudanteOpt);
	}
	
	
	@Test
	public void getStudentsByIdWhenUsernameAndPasswordAreIncorrectShouldReturnStatusCode401() {
		System.out.println("Porta utilizada: " + port);
		testRestTemplate = testRestTemplate.withBasicAuth("1", "1");
		ResponseEntity<String> response = testRestTemplate.getForEntity("/v1/protected/escolaRest/1", String.class);
		
		Assertions.assertThat(response.getStatusCodeValue()).isEqualTo(401);
	}
	
	@Test
	public void listStudentsWhenUsernameAndPasswordAreCorrectShouldReturnStatusCode200() {
		Estudante estudante1 = new Estudante(1L, "test01");
		Estudante estudante2 = new Estudante(2L, "test02");
		List<Estudante> estudantes = new ArrayList<>();
		estudantes.add(estudante1);
		estudantes.add(estudante2);
		
		BDDMockito.when(dao.findAll()).thenReturn(estudantes);
		
		ResponseEntity<String> response = testRestTemplate.getForEntity("/v1/protected/escolaRest", String.class);
		
		Assertions.assertThat(response.getStatusCodeValue()).isEqualTo(200);
	}
	
	@Test
	public void getStudentsByIdWhenUsernameAndPasswordAreCorrectShouldReturnStatusCode200() {
		testRestTemplate = testRestTemplate.withBasicAuth("1", "1");
		ResponseEntity<String> response = testRestTemplate
				.getForEntity("/v1/protected/escolaRest/{id}", String.class, 1L);
		
		Assertions.assertThat(response.getStatusCodeValue()).isEqualTo(401);
	}
	
	@Test
	public void getStudentsByIdWhenUsernameAndPasswordAreCorrectAndStudentDoesNotExistShouldReturnStatusCode404() {
		ResponseEntity<Estudante> response = testRestTemplate
				.getForEntity("/v1/protected/escolaRest/{id}", Estudante.class, -1);
		
		Assertions.assertThat(response.getStatusCodeValue()).isEqualTo(404);
	}
	
	@Test
	public void deleteWhenUserHasRoleAdminAndStudentExistShouldReturnStatusCode200() {
		 BDDMockito.doNothing().when(dao).deleteById(1L);
	     ResponseEntity<String> exchange = testRestTemplate
	    		 .exchange("/v1/admin/escolaRest/{id}", HttpMethod.DELETE, null, String.class, 1L);
	        
	     Assertions.assertThat(exchange.getStatusCodeValue()).isEqualTo(200);
	}
	
	@Test
	@WithMockUser(username="x", password="x", roles= {"USER","ADMIN"}) //é necessário apenas "roles"
	public void deleteWhenUserHasRoleAdminAndStudentDoesNotExistShouldReturnStatusCode404() throws Exception {
		Estudante estudante = new Estudante(1L, "test01");
		Optional<Estudante> estudanteOpt = dao.findById(estudante.getId());
		
		BDDMockito.when(dao.findById(estudante.getId())).thenReturn(estudanteOpt);
		
		BDDMockito.doNothing().when(dao).deleteById(1L);
//		ResponseEntity<String> exchange = testRestTemplate
//				.exchange("/v1/admin/escolaRest/{id}", HttpMethod.DELETE, null, String.class, 1L);
//		
//		Assertions.assertThat(exchange.getStatusCodeValue()).isEqualTo(404);
		mockMvc.perform(MockMvcRequestBuilders
				.delete("/v1/admin/escolaRest/{id}", 1L))
				.andExpect(MockMvcResultMatchers.status().isNotFound());
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
