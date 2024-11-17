package site.sonisori.sonisori.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import site.sonisori.sonisori.common.constants.ErrorMessage;

public record UpdateUserNameRequest(
	@NotBlank(message = ErrorMessage.INVALID_VALUE)
	@Size(max = 20, message = ErrorMessage.INVALID_VALUE)
	String name
) {
}
