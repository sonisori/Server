package site.sonisori.sonisori.exception;

import java.util.List;
import java.util.Objects;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

import lombok.extern.slf4j.Slf4j;
import site.sonisori.sonisori.common.constants.ErrorMessage;
import site.sonisori.sonisori.common.response.ErrorResponse;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
	private static final String DEFAULT_MESSAGE = ErrorMessage.INVALID_REQUEST.getMessage();

	@ExceptionHandler(HandlerMethodValidationException.class)
	public ResponseEntity<ErrorResponse> handlerMethodValidationException(HandlerMethodValidationException ex) {
		logException(ex);
		String errorMessage = ex.getAllValidationResults()
			.getFirst()
			.getResolvableErrors()
			.getFirst()
			.getDefaultMessage();

		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
			.body(new ErrorResponse(errorMessage));
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
		logException(ex);
		List<FieldError> fieldErrors = ex.getFieldErrors();
		String errorMessage = fieldErrors.stream()
			.map(FieldError::getDefaultMessage)
			.filter(Objects::nonNull)
			.findFirst()
			.orElse(ErrorMessage.METHOD_VALIDATION_FAILED.getMessage());

		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
			.body(new ErrorResponse(errorMessage));
	}

	@ExceptionHandler(NotFoundException.class)
	public ResponseEntity<ErrorResponse> handleNotFoundException(NotFoundException ex) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND)
			.body(new ErrorResponse(ex.getMessage()));
	}

	@ExceptionHandler(AuthenticationException.class)
	public ResponseEntity<ErrorResponse> handleAuthenticationException(AuthenticationException ex) {
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
			.body(new ErrorResponse(ex.getMessage()));
	}

	@ExceptionHandler(AlreadyExistException.class)
	public ResponseEntity<ErrorResponse> handleAlreadyExistException(AlreadyExistException ex) {
		ErrorResponse errorResponse = new ErrorResponse(ex.getMessage());
		return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
	}

	@ExceptionHandler(NullPointerException.class)
	public ResponseEntity<ErrorResponse> handleNullPointerException(NullPointerException ex) {
		logException(ex);
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
			.body(new ErrorResponse(ErrorMessage.NULL_POINTER_EXCEPTION.getMessage()));
	}

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
		logException(ex);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
			.body(new ErrorResponse(ex.getMessage()));
	}

	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException ex) {
		logException(ex);
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
			.body(new ErrorResponse(ErrorMessage.SERVER_ERROR.getMessage()));
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleException(Exception ex) {
		logException(ex);
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
			.body(new ErrorResponse(ErrorMessage.SERVER_ERROR.getMessage()));
	}

	private void logException(Exception ex) {
		log.error("Unhandled exception: [{}] - {}", ex.getClass().getSimpleName(), ex.getMessage());
	}
}
