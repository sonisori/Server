package site.sonisori.sonisori.dto.signword;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import site.sonisori.sonisori.common.constants.ErrorMessage;

public record SignWordRequest(
	@NotBlank(message = ErrorMessage.INVALID_VALUE)
	@Size(max = 50, message = ErrorMessage.INVALID_VALUE)
	String word,

	@Size(max = 500, message = ErrorMessage.INVALID_VALUE)
	String description
) {
}
