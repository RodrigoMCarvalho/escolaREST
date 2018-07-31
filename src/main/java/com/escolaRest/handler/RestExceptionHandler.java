package com.escolaRest.handler;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.escolaRest.error.ResourceNotFoundDetails;
import com.escolaRest.error.ResourceNotFoundException;

@ControllerAdvice
public class RestExceptionHandler {
	
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<?> handleResourceNotFoundException(ResourceNotFoundException rfnException){
		
		//personalizar o JSON
		ResourceNotFoundDetails rnfDetails = ResourceNotFoundDetails.Builder.newBuilder()
			.timestamp(new Date().getTime())
			.status(HttpStatus.NOT_FOUND.value())  //value por causa do int status
			.title("Recurso não encontrado")
			.detail(rfnException.getMessage())
			.developerMessage(rfnException.getClass().getName())
			.build();
		
		return new ResponseEntity<>(rnfDetails, HttpStatus.NOT_FOUND);
	}
}
