package com.fredericoffs.cursomc.resources.exception;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.fredericoffs.cursomc.services.exception.AuthorizationException;
import com.fredericoffs.cursomc.services.exception.DataIntegrityException;
import com.fredericoffs.cursomc.services.exception.FileException;
import com.fredericoffs.cursomc.services.exception.ObjectNotFoundException;

@ControllerAdvice
public class ResourceExceptionHandler {

	@ExceptionHandler(ObjectNotFoundException.class)
	public ResponseEntity<StandardError> resourceNotFound(ObjectNotFoundException e, HttpServletRequest request) {

		HttpStatus status = HttpStatus.NOT_FOUND;
		StandardError err = new StandardError(System.currentTimeMillis(), status.value(), "Não encontrado", e.getMessage(),
				request.getRequestURI());
		return ResponseEntity.status(status).body(err);
	}

	@ExceptionHandler(DataIntegrityException.class)
	public ResponseEntity<StandardError> dataIntegrity(DataIntegrityException e, HttpServletRequest request) {

		HttpStatus status = HttpStatus.BAD_REQUEST;
		StandardError err = new StandardError(System.currentTimeMillis(), status.value(), "Integridade de dados", e.getMessage(),
				request.getRequestURI());
		return ResponseEntity.status(status).body(err);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<StandardError> validation(MethodArgumentNotValidException e, HttpServletRequest request) {

		HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;
		ValidationError err = new ValidationError(System.currentTimeMillis(), status.value(), "Erro de validação", e.getMessage(),
				request.getRequestURI());

		for (FieldError x : e.getBindingResult().getFieldErrors()) {
			err.addError(x.getField(), x.getDefaultMessage());
		}
		return ResponseEntity.status(status).body(err);
	}

	@ExceptionHandler(AuthorizationException.class)
	public ResponseEntity<StandardError> authorization(AuthorizationException e, HttpServletRequest request) {

		HttpStatus status = HttpStatus.FORBIDDEN;
		StandardError err = new StandardError(System.currentTimeMillis(), status.value(), "Acesso negado", e.getMessage(),
				request.getRequestURI());
		return ResponseEntity.status(status).body(err);
	}

	@ExceptionHandler(FileException.class)
	public ResponseEntity<StandardError> file(FileException e, HttpServletRequest request) {

		HttpStatus status = HttpStatus.BAD_REQUEST;
		StandardError err = new StandardError(System.currentTimeMillis(), status.value(), "Erro de arquivo", e.getMessage(),
				request.getRequestURI());
		return ResponseEntity.status(status).body(err);
	}

	@ExceptionHandler(AmazonServiceException.class)
	public ResponseEntity<StandardError> file(AmazonServiceException e, HttpServletRequest request) {

		HttpStatus status = HttpStatus.valueOf(e.getErrorCode());
		StandardError err = new StandardError(System.currentTimeMillis(), status.value(), "Erro Amazon Service", e.getMessage(),
				request.getRequestURI());
		return ResponseEntity.status(status).body(err);
	}

	@ExceptionHandler(AmazonClientException.class)
	public ResponseEntity<StandardError> amazonClient(AmazonClientException e, HttpServletRequest request) {

		HttpStatus status = HttpStatus.BAD_REQUEST;
		StandardError err = new StandardError(System.currentTimeMillis(), status.value(), "Erro Amazon Client", e.getMessage(),
				request.getRequestURI());
		return ResponseEntity.status(status).body(err);
	}

	@ExceptionHandler(AmazonS3Exception.class)
	public ResponseEntity<StandardError> amazonS3Exception(AmazonS3Exception e, HttpServletRequest request) {

		HttpStatus status = HttpStatus.BAD_REQUEST;
		StandardError err = new StandardError(System.currentTimeMillis(), status.value(), "Erro S3", e.getMessage(),
				request.getRequestURI());
		return ResponseEntity.status(status).body(err);
	}
}
