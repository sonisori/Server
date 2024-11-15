package site.sonisori.sonisori.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import site.sonisori.sonisori.dto.signquiz.SignQuizResponse;
import site.sonisori.sonisori.service.SignQuizService;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class SignQuizController {

	private final SignQuizService signQuizService;

	@GetMapping("/topics/{topicId}/quizzes")
	public ResponseEntity<List<SignQuizResponse>> getQuizzesByTopicId(
		@PathVariable(name = "topicId") Long signTopicId
	) {
		List<SignQuizResponse> signQuizzes = signQuizService.findQuizzesByTopicId(signTopicId);
		return ResponseEntity.ok(signQuizzes);
	}
}
