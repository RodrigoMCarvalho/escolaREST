package com.escolaRest.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.escolaRest.model.Estudante;

@Repository
public interface EstudanteDAO extends CrudRepository<Estudante, Long> {
	
	public Estudante findByNome(String nome); //busca pelo nome digitado completo
	
	public List<Estudante> findByNomeIgnoreCaseContaining(String nome);
	//busca por nome ignorando se é maiúsculo ou minúsculo e apenas uma parte do nome
	//equivalente ao LIKE
	
}
