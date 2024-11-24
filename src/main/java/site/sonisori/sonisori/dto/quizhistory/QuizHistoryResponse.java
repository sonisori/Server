package site.sonisori.sonisori.dto.quizhistory;

public record QuizHistoryResponse(
	String title,
	int correctCount,
	int totalQuizzes
) {
}
