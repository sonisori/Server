package site.sonisori.sonisori.dto.signtopic;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import site.sonisori.sonisori.common.constants.ErrorMessage;
import site.sonisori.sonisori.common.enums.Difficulty;

public record AddSignTopicRequest(
	@NotBlank(message = ErrorMessage.INVALID_VALUE)
	@Size(max = 45, message = ErrorMessage.INVALID_VALUE)
	String topicName,

	@NotBlank(message = ErrorMessage.INVALID_VALUE)
	@Size(max = 255, message = ErrorMessage.INVALID_VALUE)
	String topicContents,

	@NotNull(message = ErrorMessage.INVALID_VALUE)
	Difficulty difficulty
) {
}
