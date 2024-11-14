package site.sonisori.sonisori.exception;

import org.springframework.security.core.AuthenticationException;

import site.sonisori.sonisori.common.constants.ErrorMessage;

public class InvalidUserException extends AuthenticationException {
	public InvalidUserException() {
		super(ErrorMessage.INVALID_USER.getMessage());
	}
}
