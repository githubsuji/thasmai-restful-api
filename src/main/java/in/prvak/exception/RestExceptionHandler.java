package in.prvak.exception;

import java.util.ArrayList;
import java.util.List;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import in.prvak.exception.types.AgentIdentifierNotfoundException;
import in.prvak.exception.types.InValidDataException;
import in.prvak.exception.types.NoContentFoundException;



@Order(Ordered.HIGHEST_PRECEDENCE)
//@EnableWebMvc
@RestControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		String error = "Malformed JSON request";
		return buildResponseEntity(new ApiError(HttpStatus.BAD_REQUEST, error, ex, HttpStatus.BAD_REQUEST.value()));
	}

	@Override
	protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		String error = "Method Not supported";
		return buildResponseEntity(
				new ApiError(HttpStatus.METHOD_NOT_ALLOWED, error, ex, HttpStatus.METHOD_NOT_ALLOWED.value()));
	}

	@Override
	protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		String error = "Media Type Not supported";
		return buildResponseEntity(
				new ApiError(HttpStatus.UNSUPPORTED_MEDIA_TYPE, error, ex, HttpStatus.UNSUPPORTED_MEDIA_TYPE.value()));
	}

	@Override
	protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers,
			HttpStatus status, WebRequest request) {
		String error = "No Service Resource handler Found";
		return buildResponseEntity(new ApiError(HttpStatus.NOT_FOUND, error, ex, HttpStatus.NOT_FOUND.value()));
	}

	@Override
	protected ResponseEntity<Object> handleAsyncRequestTimeoutException(AsyncRequestTimeoutException ex,
			HttpHeaders headers, HttpStatus status, WebRequest webRequest) {
		String error = "Gateway Timeout ";
		return buildResponseEntity(
				new ApiError(HttpStatus.GATEWAY_TIMEOUT, error, ex, HttpStatus.GATEWAY_TIMEOUT.value()));
	}

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		 BindingResult result = ex.getBindingResult();
		 List<String> validErrors = fromBindingErrors(result);
		 String error = "";
		 for(String err:validErrors) {
			 //System.out.println("error==="+err);
			 error = err;
		 }
//		String errorMsg = ex.getBindingResult().getFieldErrors().stream()
//				.map(DefaultMessageSourceResolvable::getDefaultMessage).findFirst().orElse(ex.getMessage());
		return buildResponseEntity(
				new ApiError(HttpStatus.BAD_REQUEST, error, ex, HttpStatus.BAD_REQUEST.value()));
	}
	@ExceptionHandler(NoContentFoundException.class)
	public final ResponseEntity<?> handleAllNoContentFoundException(Exception ex, WebRequest request) {
		return buildResponseEntity(
				new ApiError(HttpStatus.NO_CONTENT, ex.getMessage(), ex, HttpStatus.NO_CONTENT.value()));
	}

	@ExceptionHandler({ InValidDataException.class, AgentIdentifierNotfoundException.class })
	public final ResponseEntity<?> handleAllInValidDataException(Exception ex, WebRequest request) {
		return buildResponseEntity(
				new ApiError(HttpStatus.BAD_REQUEST, ex.getMessage(), ex, HttpStatus.BAD_REQUEST.value()));
	}

	

	@ExceptionHandler(Exception.class)
	public final ResponseEntity<?> handleAllExceptions(Exception ex, WebRequest request) {
		String error = "Internal Server Error";
		ex.printStackTrace();
		return buildResponseEntity(
				new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, error, ex, HttpStatus.INTERNAL_SERVER_ERROR.value()));
	}

	private ResponseEntity<Object> buildResponseEntity(ApiError apiError) {
		return new ResponseEntity<>(apiError, apiError.getStatus());
	}
	 private static List<String> fromBindingErrors(Errors errors) {
	        List<String> validErrors = new ArrayList<String>();
	        for (ObjectError objectError : errors.getAllErrors()) {
	            validErrors.add(objectError.getDefaultMessage());
	        }
	        return validErrors;
	    }

	/*
	 * 405 - Method Not Allowed 404 - RESOURCE NOT FOUND 400 - BAD REQUEST 401 -
	 * UNAUTHORIZED 415 - UNSUPPORTED TYPE - Representation not supported for the
	 * resource 500 - INTERNAL SERVER ERROR 504 - GATEWAY TIMEOUT 200(OK) 201 *
	 * (CREATED) 204 NO CONTENT 205 RESET CONTENT
	 */
	// https://stackoverflow.com/questions/28902374/spring-boot-rest-service-exception-handling
	// https://github.com/magiccrafter/spring-boot-exception-handling
	// https://dzone.com/articles/global-exception-handling-with-controlleradvice
}
