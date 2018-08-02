package com.escolaRest.handler;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.escolaRest.error.ErrorDetails;
import com.escolaRest.error.ResourceNotFoundDetails;
import com.escolaRest.error.ResourceNotFoundException;
import com.escolaRest.error.ValidationErrorDetails;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<?> handleResourceNotFoundException(ResourceNotFoundException rfnException) {

		// personalizar o JSON
		ResourceNotFoundDetails rnfDetails = ResourceNotFoundDetails.Builder.newBuilder()
				.timestamp(LocalDate.now())
				.status(HttpStatus.NOT_FOUND.value()) // value por causa do int status
				.title("Recurso não encontrado")
				.detail(rfnException.getMessage())
				.developerMessage(rfnException.getClass().getName())
				.build();

		return new ResponseEntity<>(rnfDetails, HttpStatus.NOT_FOUND);
	}

	@Override
	public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException manvException,
														HttpHeaders headers, 
														HttpStatus status, 
														WebRequest request) {
		// personalizar a validação
		List<FieldError> fieldErrors = manvException.getBindingResult().getFieldErrors();
		String fields = fieldErrors.stream().map(FieldError::getField).collect(Collectors.joining(","));
		String fieldMessages = fieldErrors.stream().map(FieldError::getDefaultMessage).collect(Collectors.joining(","));

		ValidationErrorDetails veDetails = ValidationErrorDetails.Builder
				.newBuilder()
				.timestamp(LocalDate.now())
				.status(HttpStatus.BAD_REQUEST.value()) // value por causa do int status
				.title("Erro de validação").detail(manvException.getMessage())
				.detail(manvException.getMessage())
				.developerMessage(manvException.getClass().getName())
				.field(fields)
				.fieldMessage(fieldMessages)
				.build();

		return new ResponseEntity<>(veDetails, HttpStatus.BAD_REQUEST);
	}

//	@Override
//	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
//														HttpHeaders headers, 
//														HttpStatus status, 
//														WebRequest request) {
//		// tratamento de erro quando o formato do Json está incorreto. Faltando aspas por exemplo
//		ErrorDetails rnfDetails = ResourceNotFoundDetails.Builder.newBuilder()
//				.timestamp(LocalDate.now())
//				.status(HttpStatus.NOT_FOUND.value()) // value por causa do int status
//				.title("Recurso não encontrado")
//				.detail(ex.getMessage())
//				.developerMessage(ex.getClass().getName())
//				.build();
//
//		return new ResponseEntity<>(rnfDetails, HttpStatus.NOT_FOUND);
//	}
	
	@Override
	protected ResponseEntity<Object> handleExceptionInternal(Exception ex, 
													@Nullable Object body, 
													HttpHeaders headers,
													HttpStatus status, 
													WebRequest request) {
		
		//tratamento de erro de forma mais genérica
		ErrorDetails rnfDetails = ResourceNotFoundDetails.Builder.newBuilder()
				.timestamp(LocalDate.now())
				.status(status.value()) // pega o status que vem como argumento
				.title("Ocorreu um erro interno.")
				.detail(ex.getMessage())
				.developerMessage(ex.getClass().getName())
				.build();

		return new ResponseEntity<>(rnfDetails, HttpStatus.NOT_FOUND);
	}
}
