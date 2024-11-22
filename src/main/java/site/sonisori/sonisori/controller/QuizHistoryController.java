package site.sonisori.sonisori.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import site.sonisori.sonisori.auth.CustomUserDetails;
import site.sonisori.sonisori.dto.quizhistory.QuizHistoryRequest;
import site.sonisori.sonisori.dto.quizhistory.QuizHistoryResponse;
import site.sonisori.sonisori.service.QuizHistoryService;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class QuizHistoryController {

	private final QuizHistoryService quizHistoryService;

	@PostMapping("/topics/{topicId}/result")
	public ResponseEntity<QuizHistoryResponse> saveAndGetQuizResult(
		@PathVariable("topicId") Long topicId,
		@AuthenticationPrincipal CustomUserDetails userDetails,
		@RequestBody @Valid QuizHistoryRequest quizHistoryRequest
	) {
		Long userId = userDetails.getUserId();
		QuizHistoryResponse response = quizHistoryService.saveAndGetQuizResult(
			userId, topicId, quizHistoryRequest
		);
		return ResponseEntity.ok(response);
	}
}
