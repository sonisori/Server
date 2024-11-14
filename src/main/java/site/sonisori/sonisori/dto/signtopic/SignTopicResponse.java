package site.sonisori.sonisori.dto.signtopic;

import lombok.Builder;
import site.sonisori.sonisori.common.enums.Difficulty;

@Builder
public record SignTopicResponse(
	Long id,
	String title,
	String contents,
	Difficulty difficulty,
	boolean isCompleted,
	int totalQuizzes,
	int count
) {
}
