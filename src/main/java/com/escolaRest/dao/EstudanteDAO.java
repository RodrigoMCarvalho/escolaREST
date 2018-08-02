package com.escolaRest.dao;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.escolaRest.model.Estudante;

@Repository       //a interface PagingAndSortingRepository extende de CrudRepository
public interface EstudanteDAO extends PagingAndSortingRepository<Estudante, Long> {
	
	public Estudante findByNome(String nome); //busca pelo nome digitado completo
	
	public List<Estudante> findByNomeIgnoreCaseContaining(String nome);
	//busca por nome ignorando se é maiúsculo ou minúsculo e apenas uma parte do nome
	//equivalente ao LIKE
	
}
