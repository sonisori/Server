package site.sonisori.sonisori.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import site.sonisori.sonisori.common.response.SuccessResponse;
import site.sonisori.sonisori.dto.signtopic.SignTopicRequest;
import site.sonisori.sonisori.dto.signtopic.SignTopicResponse;
import site.sonisori.sonisori.entity.QuizHistory;
import site.sonisori.sonisori.entity.SignTopic;
import site.sonisori.sonisori.repository.QuizHistoryRepository;
import site.sonisori.sonisori.repository.SignTopicRepository;

@Service
@RequiredArgsConstructor
public class SignTopicService {
	private final SignTopicRepository signTopicRepository;
	private final QuizHistoryRepository quizHistoryRepository;

	public List<SignTopicResponse> fetchTopicsWithQuizProgress(Long userId) {
		List<SignTopic> signTopics = signTopicRepository.findAll();

		Map<Long, QuizHistory> quizHistoryMap = quizHistoryRepository.findByUser_id(userId)
			.stream()
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
		int correctCount = Optional.ofNullable(quizHistory)
			.map(QuizHistory::getCorrectCount)
			.orElse(0);
		return SignTopicResponse.builder()
			.id(signTopic.getId())
			.title(signTopic.getTitle())
			.contents(signTopic.getContents())
			.difficulty(signTopic.getDifficulty())
			.isCompleted(quizHistory != null)
			.totalQuizzes(signTopic.getTotalQuizzes())
			.correctCount(correctCount)
			.build();
	}

	public SuccessResponse addSignTopic(SignTopicRequest signTopicRequest) {
		SignTopic signTopic = SignTopic.builder()
			.title(signTopicRequest.title())
			.contents(signTopicRequest.contents())
			.difficulty(signTopicRequest.difficulty())
			.build();

		Long id = signTopicRepository.save(signTopic).getId();

		return new SuccessResponse(id);
	}
}
