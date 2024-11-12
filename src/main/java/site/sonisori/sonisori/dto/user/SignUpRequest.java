package site.sonisori.sonisori.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import site.sonisori.sonisori.common.constants.ErrorMessage;

public record SignUpRequest(
	@NotBlank(message = ErrorMessage.INVALID_VALUE)
	String name,

	@NotBlank(message = ErrorMessage.INVALID_VALUE)
	@Email(message = ErrorMessage.INVALID_VALUE)
	String email,

	@NotBlank(message = ErrorMessage.INVALID_VALUE)
	String password
) {
}
