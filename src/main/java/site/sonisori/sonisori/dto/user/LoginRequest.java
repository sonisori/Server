package site.sonisori.sonisori.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import site.sonisori.sonisori.common.constants.ErrorMessage;

public record LoginRequest(
	@Email(message = ErrorMessage.INVALID_VALUE)
	String email,

	@NotBlank(message = ErrorMessage.INVALID_VALUE)
	String password
) {
}
