package com.escolaRest.resource;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.escolaRest.dao.EstudanteDAO;
import com.escolaRest.error.CustomErrorType;
import com.escolaRest.error.ResourceNotFoundException;
import com.escolaRest.model.Estudante;

@RestController
@RequestMapping("/v1")
public class EstudanteResource {

	@Autowired
	private EstudanteDAO dao;
	
	@GetMapping("/protected/escolaRest")
	public ResponseEntity<?> buscaTodos(Pageable paginacao){
		System.out.println(dao.findAll());
		return new ResponseEntity<>(dao.findAll(paginacao), HttpStatus.OK);
	}
	
	@GetMapping("/protected/escolaRest/{id}")
	public ResponseEntity<?> buscaPorId(@PathVariable("id") Long id,
										@AuthenticationPrincipal UserDetails userDetails){
		System.out.println(userDetails); //para verificar os dados do usuário
		verificaSeEstudanteExiste(id);
		Optional<Estudante> estudante = dao.findById(id);
		return new ResponseEntity<>(estudante, HttpStatus.OK);
	}
	
	@GetMapping("/protected/escolaRest/estudante/{nome}")
	public ResponseEntity<?> buscaPorNome(@PathVariable("nome") String nome){
		List<Estudante> estudante = dao.findByNomeIgnoreCaseContaining(nome);
		if (estudante.isEmpty()) {
			return new ResponseEntity<>(new CustomErrorType("Estudante não encontrado!"), HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(estudante, HttpStatus.OK);
	}
	
	@PostMapping("/admin/escolaRest")
	public ResponseEntity<?> salvar(@Valid @RequestBody Estudante estudante){
		return new ResponseEntity<>(dao.save(estudante), HttpStatus.OK);
	}
	
	@DeleteMapping("/admin/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> removerPorId(@PathVariable("id") Long id){
		verificaSeEstudanteExiste(id);
		dao.deleteById(id);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@PutMapping("/admin/escolaRest")
	public ResponseEntity<?> atualizar(@RequestBody Estudante estudante){
		verificaSeEstudanteExiste(estudante.getId());
		return new ResponseEntity<>(dao.save(estudante), HttpStatus.OK);
	}
	
	private void verificaSeEstudanteExiste(Long id) {
		Optional<Estudante> estudante = dao.findById(id);
		
		if (!estudante.isPresent()) {   //se não tiver nada presente em estudante, devido ao Optional
			//return new ResponseEntity<>(new CustomErrorType("Estudante não encontrado!"), HttpStatus.NOT_FOUND);
			throw new ResourceNotFoundException("Não foi encontrado um estudante para o ID: " + id);
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
}
