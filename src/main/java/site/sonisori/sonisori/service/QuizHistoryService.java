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

		if (quizHistoryRequest.correctCount() > signTopic.getTotalQuizzes()) {
			throw new IllegalArgumentException(ErrorMessage.EXCEEDS_TOTAL_COUNT.getMessage());
		}
		QuizHistory quizHistory = QuizHistory.builder()
			.user(user)
			.signTopic(signTopic)
			.correctCount(quizHistoryRequest.correctCount())
			.build();
		quizHistoryRepository.save(quizHistory);

		return new QuizHistoryResponse(
			signTopic.getTitle(),
			quizHistoryRequest.correctCount(),
			signTopic.getTotalQuizzes()
		);
	}
}
