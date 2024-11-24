package site.sonisori.sonisori.dto.quizhistory;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import site.sonisori.sonisori.common.constants.ErrorMessage;

public record QuizHistoryRequest(
	@NotNull(message = ErrorMessage.INVALID_VALUE)
	@Min(value = 0, message = ErrorMessage.INVALID_VALUE)
	int correctCount
) {
}
