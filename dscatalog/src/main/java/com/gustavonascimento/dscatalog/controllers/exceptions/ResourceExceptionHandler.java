package com.gustavonascimento.dscatalog.controllers.exceptions;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.gustavonascimento.dscatalog.services.exceptions.DataBaseException;
import com.gustavonascimento.dscatalog.services.exceptions.ResourceNotFoundException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;

@ControllerAdvice
public class ResourceExceptionHandler {

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<StandardError> entityNotFound(ResourceNotFoundException e, HttpServletRequest request) {
		StandardError err = new StandardError();
		err.setTimestamp(Instant.now());
		err.setStatus(HttpStatus.NOT_FOUND.value());
		err.setError("Recurso não encontrado");
		err.setMessage(e.getMessage());
		err.setPath(request.getRequestURI());
		return ResponseEntity.status(HttpStatus.NOT_FOUND.value()).body(err);
	}

	@ExceptionHandler(DataBaseException.class)
	public ResponseEntity<StandardError> dataBaseViolation(DataBaseException e, HttpServletRequest request) {
		StandardError err = new StandardError();
		err.setTimestamp(Instant.now());
		err.setStatus(HttpStatus.CONFLICT.value());
		err.setError("Exceção de database");
		err.setMessage(e.getMessage());
		err.setPath(request.getRequestURI());
		return ResponseEntity.status(HttpStatus.CONFLICT.value()).body(err);
	}
	
	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<ValidationError> validation(ConstraintViolationException e, HttpServletRequest request) {
		HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;
		ValidationError err = new ValidationError();
		err.setTimestamp(Instant.now());
		err.setStatus(status.value());
		err.setError("Exceção de validação");
		err.setMessage("Ops! Alguma coisa deu errado");
		err.setPath(request.getRequestURI());
		
		List<FieldMessage> fieldMessages = e.getConstraintViolations()
	            .stream()
	            .map(violation -> new FieldMessage(
	                    violation.getPropertyPath().toString(),
	                    violation.getMessage()))
	            .collect(Collectors.toList());
		
		err.setErrors(fieldMessages);
		
		return ResponseEntity.status(status).body(err);
	}

}
