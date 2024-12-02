package site.sonisori.sonisori.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import site.sonisori.sonisori.common.response.SuccessResponse;
import site.sonisori.sonisori.dto.signquiz.SignQuizRequest;
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

	@PostMapping("/admin/topics/{topicId}/quizzes")
	public ResponseEntity<SuccessResponse> addQuizToTopicByAdmin(@PathVariable(name = "topicId") Long topicId,
		@Valid @RequestBody SignQuizRequest signQuizRequest
	) {
		SuccessResponse successResponse = signQuizService.addQuiz(topicId, signQuizRequest);
		return ResponseEntity.status(HttpStatus.CREATED).body(successResponse);
	}

	@DeleteMapping("/admin/quizzes/{quizId}")
	public ResponseEntity<Void> deleteQuizByAdmin(@PathVariable(name = "quizId") Long quizId
	) {
		signQuizService.deleteQuiz(quizId);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
}
