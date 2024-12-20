package site.sonisori.sonisori.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import site.sonisori.sonisori.common.constants.ErrorMessage;
import site.sonisori.sonisori.common.response.SuccessResponse;
import site.sonisori.sonisori.dto.signquiz.SignQuizRequest;
import site.sonisori.sonisori.dto.signquiz.SignQuizResponse;
import site.sonisori.sonisori.entity.SignQuiz;
import site.sonisori.sonisori.entity.SignTopic;
import site.sonisori.sonisori.exception.NotFoundException;
import site.sonisori.sonisori.repository.SignQuizRepository;
import site.sonisori.sonisori.repository.SignTopicRepository;

@Service
@RequiredArgsConstructor
public class SignQuizService {
	private final SignQuizRepository signQuizRepository;
	private final SignTopicRepository signTopicRepository;

	public List<SignQuizResponse> findQuizzesByTopicId(
		Long signTopicId
	) {
		signTopicRepository.findById(signTopicId)
			.orElseThrow(() -> new NotFoundException(ErrorMessage.NOT_FOUND_TOPIC.getMessage()));

		List<SignQuiz> signQuizzes = signQuizRepository.findAllBySignTopic_id(signTopicId);

		return signQuizzes.stream()
			.map(quiz -> new SignQuizResponse(
				quiz.getId(), quiz.getSentence()
			))
			.collect(Collectors.toList());
	}

	@Transactional
	public SuccessResponse addQuiz(Long topicId, SignQuizRequest signQuizRequest) {
		SignTopic signTopic = signTopicRepository.findById(topicId)
			.orElseThrow(() -> new NotFoundException(ErrorMessage.NOT_FOUND_TOPIC.getMessage()));

		SignQuiz signQuiz = SignQuiz.builder()
			.signTopic(signTopic)
			.sentence(signQuizRequest.sentence())
			.build();

		Long id = signQuizRepository.save(signQuiz).getId();

		signTopic.incrementTotalQuizzes();

		return new SuccessResponse(id);
	}

	@Transactional
	public void deleteQuiz(Long quizId) {
		SignQuiz signQuiz = signQuizRepository.findById(quizId)
			.orElseThrow(() -> new NotFoundException(ErrorMessage.NOT_FOUND_QUIZ.getMessage()));

		signQuiz.getSignTopic().decrementTotalQuizzes();

		signQuizRepository.deleteById(quizId);
	}

	@Transactional
	public void updateQuiz(Long quizId, SignQuizRequest signQuizRequest) {
		SignQuiz signQuiz = signQuizRepository.findById(quizId)
			.orElseThrow(() -> new NotFoundException(ErrorMessage.NOT_FOUND_QUIZ.getMessage()));

		signQuiz.updateSentence(signQuizRequest.sentence());
	}
}
