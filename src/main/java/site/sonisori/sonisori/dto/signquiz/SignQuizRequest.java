package site.sonisori.sonisori.dto.signquiz;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import site.sonisori.sonisori.common.constants.ErrorMessage;

public record SignQuizRequest(
	@NotBlank(message = ErrorMessage.INVALID_VALUE)
	@Size(max = 255, message = ErrorMessage.INVALID_VALUE)
	String sentence
) {

}
