package site.sonisori.sonisori.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import site.sonisori.sonisori.dto.signtopic.SignTopicResponse;
import site.sonisori.sonisori.entity.QuizHistory;
import site.sonisori.sonisori.entity.SignTopic;
import site.sonisori.sonisori.repository.QuizHistoryRepository;
import site.sonisori.sonisori.repository.SignQuizRepository;
import site.sonisori.sonisori.repository.SignTopicRepository;

@Service
@RequiredArgsConstructor
public class SignTopicService {
	private final SignTopicRepository signTopicRepository;
	private final QuizHistoryRepository quizHistoryRepository;
	private final SignQuizRepository signQuizRepository;

	public List<SignTopicResponse> getTopicsWithStatus(Long userId) {
		List<SignTopic> signTopics = signTopicRepository.findAll();
		List<QuizHistory> quizHistories = quizHistoryRepository.findByUser_id(userId);

		return signTopics.stream().map(
			signTopic -> {
				QuizHistory quizHistory = quizHistories.stream()
					.filter(history -> history.getSignTopic().getId().equals(signTopic.getId()))
					.findFirst()
					.orElse(null);

				int totalQuizzes = signQuizRepository.countBySignTopic_id(signTopic.getId());
				int count = (quizHistory != null) ? quizHistory.getCount() : 0;

				return SignTopicResponse.builder()
					.id(signTopic.getId())
					.title(signTopic.getTitle())
					.contents(signTopic.getContents())
					.difficulty(signTopic.getDifficulty())
					.isCompleted(quizHistory != null)
					.totalQuizzes(totalQuizzes)
					.count(count)
					.build();
			}).collect(Collectors.toList());
	}
}
