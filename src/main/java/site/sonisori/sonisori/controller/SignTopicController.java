package site.sonisori.sonisori.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import site.sonisori.sonisori.auth.CustomUserDetails;
import site.sonisori.sonisori.dto.signtopic.SignTopicResponse;
import site.sonisori.sonisori.service.SignTopicService;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class SignTopicController {

	private final SignTopicService signTopicService;

	@GetMapping("/topics")
	public ResponseEntity<List<SignTopicResponse>> getAllTopicsWithUserProgress(
		@AuthenticationPrincipal CustomUserDetails userDetails
	) {
		Long userId = userDetails.getUserId();
		List<SignTopicResponse> topics = signTopicService.fetchTopicsWithQuizProgress(userId);
		return ResponseEntity.ok(topics);
	}
}
