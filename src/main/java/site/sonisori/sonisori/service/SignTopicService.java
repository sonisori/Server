package site.sonisori.sonisori.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import site.sonisori.sonisori.common.constants.ErrorMessage;
import site.sonisori.sonisori.common.response.SuccessResponse;
import site.sonisori.sonisori.dto.signtopic.SignTopicRequest;
import site.sonisori.sonisori.dto.signtopic.SignTopicResponse;
import site.sonisori.sonisori.entity.QuizHistory;
import site.sonisori.sonisori.entity.SignTopic;
import site.sonisori.sonisori.exception.NotFoundException;
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

	@Transactional
	public SuccessResponse addSignTopic(SignTopicRequest signTopicRequest) {
		SignTopic signTopic = SignTopic.builder()
			.title(signTopicRequest.title())
			.contents(signTopicRequest.contents())
			.difficulty(signTopicRequest.difficulty())
			.build();

		Long id = signTopicRepository.save(signTopic).getId();

		return new SuccessResponse(id);
	}

	public void deleteSignTopic(Long topicId) {
		if (!signTopicRepository.existsById(topicId)) {
			throw new NotFoundException(ErrorMessage.NOT_FOUND_TOPIC.getMessage());
		}

		signTopicRepository.deleteById(topicId);
	}

	@Transactional
	public void updateSignTopic(Long topicId, SignTopicRequest signTopicRequest) {
		SignTopic signTopic = signTopicRepository.findById(topicId)
			.orElseThrow(() -> new NotFoundException(ErrorMessage.NOT_FOUND_TOPIC.getMessage()));

		signTopic.updateTopic(signTopicRequest.title(), signTopicRequest.contents(), signTopicRequest.difficulty());
	}
}
