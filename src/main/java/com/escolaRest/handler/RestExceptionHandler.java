package com.escolaRest.handler;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.escolaRest.error.ResourceNotFoundDetails;
import com.escolaRest.error.ResourceNotFoundException;
import com.escolaRest.error.ValidationErrorDetails;

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
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException manvException){
		
		//personalizar a validação
		List<FieldError> fieldErrors = manvException.getBindingResult().getFieldErrors();
		String fields = fieldErrors.stream().map(FieldError::getField).collect(Collectors.joining(","));
		String fieldMessages = fieldErrors.stream().map(FieldError::getDefaultMessage).collect(Collectors.joining(","));
		
		ValidationErrorDetails veDetails = ValidationErrorDetails.Builder.newBuilder()
			.timestamp(new Date().getTime())
			.status(HttpStatus.BAD_REQUEST.value())  //value por causa do int status
			.title("Erro de validação")
			.detail(manvException.getMessage())
			.developerMessage(manvException.getClass().getName())
			.field(fields)
			.fieldMessage(fieldMessages)
			.build();
		
		return new ResponseEntity<>(veDetails, HttpStatus.BAD_REQUEST);
	}
}
