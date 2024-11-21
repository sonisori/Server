package site.sonisori.sonisori.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import site.sonisori.sonisori.auth.CustomUserDetails;
import site.sonisori.sonisori.dto.signtopic.AddSignTopicRequest;
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

	@PostMapping("/admin/topics")
	public ResponseEntity<Map<String, Long>> addSignTopic(@Valid @RequestBody AddSignTopicRequest signTopicRequest) {
		Map<String, Long> topicId = Map.of("topicId", signTopicService.addSignTopic(signTopicRequest));

		return ResponseEntity.status(HttpStatus.CREATED).body(topicId);
	}
}
