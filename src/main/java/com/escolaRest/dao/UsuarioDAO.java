package com.escolaRest.dao;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.escolaRest.model.Usuario;

public interface UsuarioDAO extends PagingAndSortingRepository<Usuario, Long>{
	Usuario findByLogin(String login);
}
