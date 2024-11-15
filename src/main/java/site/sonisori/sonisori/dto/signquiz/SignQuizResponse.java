package site.sonisori.sonisori.dto.signquiz;

import lombok.Builder;

@Builder
public record SignQuizResponse(
	Long quizId,
	String sentence
) {
}
