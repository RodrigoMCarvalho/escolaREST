package com.escolaRest.resource;

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
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.escolaRest.dao.EstudanteDAO;
import com.escolaRest.model.Estudante;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT) //utilizar uma porta aleatória a cada teste
@AutoConfigureMockMvc
public class EstudanteResourceTokenTest {

	@Autowired
	private TestRestTemplate testRestTemplate;
	
	@Autowired
	private MockMvc mockMvc;  //pode subustituir o TestRestTemplate
	
	@LocalServerPort
	private int port;
	
	@MockBean
	private EstudanteDAO dao;
	
	private HttpEntity<Void> protectedHeader;
	private HttpEntity<Void> adminHeader;
	private HttpEntity<Void> wrongHeader;
	
	@Before
	public void configProtectedHeader() {
		String str = "{\"login\":\"gugu\", \"senha\": \"123\"}";
		HttpHeaders headers = testRestTemplate.postForEntity("/login", str, String.class).getHeaders();
		this.protectedHeader = new HttpEntity<>(headers);
	}
	
	@Before
	public void configAdminHeader() {
		String str = "{\"login\":\"rodrigo\", \"senha\": \"123\"}";
		HttpHeaders headers = testRestTemplate.postForEntity("/login", str, String.class).getHeaders();
		this.adminHeader = new HttpEntity<>(headers);
	}
	
	@Before
	public void configWrongHeader() {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "11111");  //valor aleatório
		this.wrongHeader = new HttpEntity<>(headers);
	}
	
	@Before
	public void setup() {
		Estudante estudante = new Estudante(1L, "test01");
		Optional<Estudante> estudanteOpt = dao.findById(estudante.getId());
		
		BDDMockito.when(dao.findById(estudante.getId())).thenReturn(estudanteOpt);
	}
	
	
	@Test
	public void getStudentsByIdWhenTokenIsIncorrectShouldReturnStatusCode403() {
		ResponseEntity<String> response = testRestTemplate
				.exchange("/v1/protected/escolaRest/1", HttpMethod.GET,wrongHeader, String.class);
		
		Assertions.assertThat(response.getStatusCodeValue()).isEqualTo(403);
	}
	
	@Test
	public void listStudentsWhenTokenIsIncorrectShouldReturnStatusCode403() {
		ResponseEntity<String> response = testRestTemplate
				.exchange("/v1/protected/escolaRest/", HttpMethod.GET,wrongHeader, String.class);
		
		Assertions.assertThat(response.getStatusCodeValue()).isEqualTo(403);
	}
	
	@Test
	public void listStudentsWhenTokenIsCorrectShouldReturnStatusCode200() {
		ResponseEntity<String> response = testRestTemplate
				.exchange("/v1/protected/escolaRest/1", HttpMethod.GET,protectedHeader, String.class);
		
		Assertions.assertThat(response.getStatusCodeValue()).isEqualTo(200);
	}
	
	@Test
	public void getStudentsByIdWhenTokenIsCorrectShouldReturnStatusCode200() {
		ResponseEntity<Estudante> response = testRestTemplate
				.exchange("/v1/protected/escolaRest/1", HttpMethod.GET,protectedHeader, Estudante.class);
		
		Assertions.assertThat(response.getStatusCodeValue()).isEqualTo(200);
	}
	
	@Test
	public void getStudentsByIdWhenTokenIsCorrectAndStudentDoesNotExistShouldReturnStatusCode404() {
		ResponseEntity<Estudante> response = testRestTemplate
				.exchange("/v1/protected/escolaRest/-1", HttpMethod.GET, protectedHeader, Estudante.class);
		
		Assertions.assertThat(response.getStatusCodeValue()).isEqualTo(404);
	}
	
	@Test
	public void deleteWhenUserHasRoleAdminAndStudentExistsShouldReturnStatusCode200() {
		BDDMockito.doNothing().when(dao).deleteById(1L);
        ResponseEntity<String> exchange = testRestTemplate
        		.exchange("/v1/admin/escolaRest/1",HttpMethod.DELETE,adminHeader, String.class);
        
        Assertions.assertThat(exchange.getStatusCodeValue()).isEqualTo(200);
	}
	
	@Test
	public void deleteWhenUserHasRoleAdminAndStudentDoesNotExistShouldReturnStatusCode404() throws Exception {
	        String token = adminHeader.getHeaders().get("Authorization").get(0);
	        BDDMockito.doNothing().when(dao).deleteById(1L);
	        mockMvc.perform(MockMvcRequestBuilders
	                .delete("/v1/admin/escolaRest/{id}", -1L).header("Authorization",token))
	       .andExpect(MockMvcResultMatchers.status().isNotFound());
	}
	
	 @Test
	    public void deleteWhenUserDoesNotHaveRoleAdminShouldReturnStatusCode403() throws Exception {
	        String token = protectedHeader.getHeaders().get("Authorization").get(0);
	        BDDMockito.doNothing().when(dao).deleteById(1L);
	        mockMvc.perform(MockMvcRequestBuilders
	                .delete("/v1/admin/escolaRest/{id}", 1L).header("Authorization",token))
	                .andExpect(MockMvcResultMatchers.status().isForbidden());
	    }

	    @Test
	    public void createWhenNameIsNullShouldReturnStatusCode400BadRequest() throws Exception {
	        Estudante estudante = new Estudante(3L, null);
	        BDDMockito.when(dao.save(estudante)).thenReturn(estudante);
	        ResponseEntity<String> response = testRestTemplate
	        		.exchange("/v1/admin/escolaRest/", HttpMethod.POST, 
	        				new HttpEntity<>(estudante,adminHeader.getHeaders()), String.class);
	        
	        Assertions.assertThat(response.getStatusCodeValue()).isEqualTo(400);
	        Assertions.assertThat(response.getBody()).contains("fieldMessage", "O nome do estudante é obrigatório");
	    }

	    @Test
	    public void createShouldPersistDataAndReturnStatusCode201() throws Exception {
	    	Estudante estudante = new Estudante(1L, "rodrigo");
	    	BDDMockito.when(dao.save(estudante)).thenReturn(estudante);
	        ResponseEntity<Estudante> response = testRestTemplate
	        		.exchange("/v1/admin/escolaRest/",HttpMethod.POST, 
	        				new HttpEntity<>(estudante, adminHeader.getHeaders()), Estudante.class);
	        
	        Assertions.assertThat(response.getStatusCodeValue()).isEqualTo(201);
	        Assertions.assertThat(response.getBody().getId()).isNotNull();
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
