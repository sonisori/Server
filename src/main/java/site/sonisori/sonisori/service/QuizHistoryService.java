package site.sonisori.sonisori.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import site.sonisori.sonisori.common.constants.ErrorMessage;
import site.sonisori.sonisori.dto.quizhistory.QuizHistoryRequest;
import site.sonisori.sonisori.dto.quizhistory.QuizHistoryResponse;
import site.sonisori.sonisori.entity.QuizHistory;
import site.sonisori.sonisori.entity.SignTopic;
import site.sonisori.sonisori.entity.User;
import site.sonisori.sonisori.exception.NotFoundException;
import site.sonisori.sonisori.repository.QuizHistoryRepository;
import site.sonisori.sonisori.repository.SignTopicRepository;
import site.sonisori.sonisori.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class QuizHistoryService {
	private final QuizHistoryRepository quizHistoryRepository;
	private final SignTopicRepository signTopicRepository;
	private final UserRepository userRepository;

	@Transactional
	public QuizHistoryResponse saveAndGetQuizResult(
		Long userId, Long topicId, QuizHistoryRequest quizHistoryRequest
	) {
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new NotFoundException(ErrorMessage.NOT_FOUND_USER.getMessage()));
		SignTopic signTopic = signTopicRepository.findById(topicId)
			.orElseThrow(() -> new NotFoundException(ErrorMessage.NOT_FOUND_TOPIC.getMessage()));

		validateCorrectCount(quizHistoryRequest.correctCount(), signTopic.getTotalQuizzes());

		QuizHistory quizHistory = updateOrSaveQuizHistory(
			user, signTopic, quizHistoryRequest.correctCount()
		);

		quizHistoryRepository.save(quizHistory);

		return new QuizHistoryResponse(
			signTopic.getTitle(),
			quizHistoryRequest.correctCount(),
			signTopic.getTotalQuizzes()
		);
	}

	private void validateCorrectCount(int correctCount, int totalQuizzes) {
		if (correctCount > totalQuizzes) {
			throw new IllegalArgumentException(ErrorMessage.EXCEEDS_TOTAL_COUNT.getMessage());
		}
	}

	private QuizHistory updateOrSaveQuizHistory(
		User user,
		SignTopic signTopic,
		int correctCount
	) {
		return quizHistoryRepository.findByUser_idAndSignTopic_id(user.getId(), signTopic.getId())
			.map(quizHistory -> {
				quizHistory.updateCorrectCount(correctCount);
				return quizHistory;
			})
			.orElseGet(() -> new QuizHistory(user, signTopic, correctCount));
	}
}
