package site.sonisori.sonisori.exception;

import site.sonisori.sonisori.common.constants.ErrorMessage;

public class UserNotFoundException extends NotFoundException {
	public UserNotFoundException() {
		super(ErrorMessage.NOT_FOUND_USER.getMessage());
	}
}
