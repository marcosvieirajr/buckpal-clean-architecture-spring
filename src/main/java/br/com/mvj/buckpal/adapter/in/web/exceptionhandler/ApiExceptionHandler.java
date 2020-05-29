package br.com.mvj.buckpal.adapter.in.web.exceptionhandler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.persistence.EntityNotFoundException;
import java.time.OffsetDateTime;

import static java.util.stream.Collectors.toList;

@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

	@Autowired
	private MessageSource messageSource;

//	@ExceptionHandler(BaseBusinesException.class)
//	public ResponseEntity<Object> handleBaseBusinesException(BaseBusinesException ex, WebRequest request){
//
//		var status = HttpStatus.BAD_REQUEST;
//		var problema = new Problema();
//		problema.setStatus(status.value());
//		problema.setMessage(ex.getMessage());
//		problema.setDateHora(OffsetDateTime.now());
//
//		return this.handleExceptionInternal(ex, problema, new HttpHeaders(), status, request);
//	}

	@ExceptionHandler(EntityNotFoundException.class)
	public ResponseEntity<Object> handleEntityNotFoundException(EntityNotFoundException ex, WebRequest request){

		var status = HttpStatus.NOT_FOUND;
		var problema = new Problema();
		problema.setStatus(status.value());
		problema.setMessage(ex.getMessage());
		problema.setDateHora(OffsetDateTime.now());

		return this.handleExceptionInternal(ex, problema, new HttpHeaders(), status, request);
	}

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {

		var campos = ex.getBindingResult().getAllErrors().stream()
				.map(error -> {
			var name = ((FieldError) error).getField();
			var message = messageSource.getMessage(error, LocaleContextHolder.getLocale());
			return new Problema.Campo(name, message);
		}).collect(toList());

		var body = new Problema();
		body.setStatus(status.value());
		body.setMessage("Um ou mais campos invalidos");
		body.setDateHora(OffsetDateTime.now());
		body.setCampos(campos);

		return super.handleExceptionInternal(ex, body, headers, status, request);
	}

}
