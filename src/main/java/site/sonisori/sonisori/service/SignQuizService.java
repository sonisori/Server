package site.sonisori.sonisori.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import site.sonisori.sonisori.common.constants.ErrorMessage;
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
		SignTopic signTopic = signTopicRepository.findById(signTopicId)
			.orElseThrow(()-> new NotFoundException(ErrorMessage.NOT_FOUND_TOPIC.getMessage()));
		List<SignQuiz> signQuizzes = signQuizRepository.findAllBySignTopic_id(signTopicId);

		return signQuizzes.stream()
			.map(quiz -> new SignQuizResponse(
				quiz.getId(), quiz.getSentence()
			))
			.collect(Collectors.toList());
	}
}
