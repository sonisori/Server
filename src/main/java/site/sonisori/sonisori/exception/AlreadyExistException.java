package site.sonisori.sonisori.exception;

import org.springframework.dao.DuplicateKeyException;

public class AlreadyExistException extends DuplicateKeyException {
	public AlreadyExistException(String message) {
		super(message);
	}
}
