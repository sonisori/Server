package site.sonisori.sonisori.service;

import java.util.List;
import java.util.Map;
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

	public List<SignTopicResponse> fetchTopicsWithQuizProgress(Long userId) {
		List<SignTopic> signTopics = signTopicRepository.findAll();
		List<QuizHistory> quizHistories = quizHistoryRepository.findByUser_id(userId);

		Map<Long, QuizHistory> quizHistoryMap = quizHistories.stream()
			.collect(Collectors.toMap(qh -> qh.getSignTopic().getId(), qh -> qh));

		return signTopics.stream()
			.map(signTopic -> buildSignTopicResponse(signTopic, quizHistoryMap))
			.collect(Collectors.toList());
	}

	private SignTopicResponse buildSignTopicResponse(
		SignTopic signTopic,
		Map<Long, QuizHistory> quizHistoryMap
	) {
		QuizHistory quizHistory = quizHistoryMap.get(signTopic.getId());
		int totalQuizzes = signQuizRepository.countBySignTopic_id(signTopic.getId());
		int correctCount = (quizHistory != null) ? quizHistory.getCount() : 0;
		return SignTopicResponse.builder()
			.id(signTopic.getId())
			.title(signTopic.getTitle())
			.contents(signTopic.getContents())
			.difficulty(signTopic.getDifficulty())
			.isCompleted(quizHistory != null)
			.totalQuizzes(totalQuizzes)
			.correctCount(correctCount)
			.build();
	}
}
