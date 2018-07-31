package com.escolaRest.resource;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
@RequestMapping("/escolaRest")
public class EstudanteResource {

	@Autowired
	private EstudanteDAO dao;
	
	@GetMapping
	public ResponseEntity<?> buscaTodos(){
		return new ResponseEntity<>(dao.findAll(), HttpStatus.OK);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<?> buscaPorId(@PathVariable("id") Long id){
		verificaSeEstudanteExiste(id);
		Optional<Estudante> estudante = dao.findById(id);
		return new ResponseEntity<>(estudante, HttpStatus.OK);
	}
	
	@GetMapping("/estudante/{nome}")
	public ResponseEntity<?> buscaPorNome(@PathVariable("nome") String nome){
		List<Estudante> estudante = dao.findByNomeIgnoreCaseContaining(nome);
		if (estudante.isEmpty()) {
			return new ResponseEntity<>(new CustomErrorType("Estudante não encontrado!"), HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(estudante, HttpStatus.OK);
	}
	
	@PostMapping
	public ResponseEntity<?> salvar(@RequestBody Estudante estudante){
		return new ResponseEntity<>(dao.save(estudante), HttpStatus.OK);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> removerPorId(@PathVariable("id") Long id){
		verificaSeEstudanteExiste(id);
		dao.deleteById(id);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@PutMapping()
	public ResponseEntity<?> atualizar(@RequestBody Estudante estudante){
		verificaSeEstudanteExiste(estudante.getId());
		return new ResponseEntity<>(dao.save(estudante), HttpStatus.OK);
	}
	
	private void verificaSeEstudanteExiste(Long id) {
		Optional<Estudante> estudante = dao.findById(id);
		
		if (!estudante.isPresent()) {   //se não tiver nada presente em estudante
			//return new ResponseEntity<>(new CustomErrorType("Estudante não encontrado!"), HttpStatus.NOT_FOUND);
			throw new ResourceNotFoundException("Não foi encontrado um estudante para o ID: " + id);
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
}
