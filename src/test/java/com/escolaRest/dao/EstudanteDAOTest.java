package com.escolaRest.dao;

import java.util.List;

import javax.validation.ConstraintViolationException;

import org.assertj.core.api.Assertions;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.escolaRest.model.Estudante;

@RunWith(SpringRunner.class)
@DataJpaTest
public class EstudanteDAOTest {

	@Autowired
	private EstudanteDAO dao;
	
	@Rule
	public ExpectedException thrown = ExpectedException.none();
	
	@Test
	@Ignore
	public void createShouldPersistData() {
		Estudante estudante = new Estudante("Rodrigo");
		dao.save(estudante);
		
		Assertions.assertThat(estudante.getId()).isNotNull();
		Assertions.assertThat(estudante.getNome()).isEqualTo("Rodrigo");
	}
	
	@Test
	@Ignore
	public void deleteShouldRemoveData() {
		Estudante estudante = new Estudante("Rodrigo");
		dao.save(estudante);
		dao.delete(estudante);
		
		Assertions.assertThat(dao.findById(estudante.getId())).isNotNull();
	}
	
	@Test
	@Ignore
	public void updateShouldChangeAndPersitData() {
		Estudante estudante = new Estudante("Rodrigo");
		dao.save(estudante);
		estudante.setNome("Gustavo");
		estudante = dao.save(estudante);
		
		Assertions.assertThat(estudante.getNome()).isEqualTo("Gustavo");
	}
	
	@Test
	@Ignore
	public void findByNomeIgnoreCaseContainingShouldIgnoreCase() {
		Estudante estudante = new Estudante("Rodrigo");
		Estudante estudante2 = new Estudante("rodrigo");
		dao.save(estudante);
		dao.save(estudante2);
		List<Estudante> estudantes = dao.findByNomeIgnoreCaseContaining("r");
		
		Assertions.assertThat(estudantes.size()).isEqualTo(2);
	}
	
	@Test
	public void createWhenNameIsNullShouldThrowConstraintViolationException() {
		thrown.expect(ConstraintViolationException.class);
		thrown.expectMessage("O nome do estudante é obrigatório");
		dao.save(new Estudante());
	}
	
	
	
	
	
	
	
	
	
	
	
}
