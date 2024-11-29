package site.sonisori.sonisori.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import site.sonisori.sonisori.auth.CustomUserDetails;
import site.sonisori.sonisori.common.response.SuccessResponse;
import site.sonisori.sonisori.dto.signtopic.SignTopicRequest;
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
	public ResponseEntity<SuccessResponse> addSignTopic(@Valid @RequestBody SignTopicRequest signTopicRequest) {
		SuccessResponse successResponse = signTopicService.addSignTopic(signTopicRequest);
		return ResponseEntity.status(HttpStatus.CREATED).body(successResponse);
	}

	@DeleteMapping("/admin/topics/{topicId}")
	public ResponseEntity<Void> deleteSignTopic(@PathVariable(name = "topicId") Long topicId) {
		signTopicService.deleteSignTopic(topicId);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

	@PatchMapping("/admin/topics/{topicId}")
	public ResponseEntity<Void> updateSignTopic(@PathVariable(name = "topicId") Long topicId,
		@Valid @RequestBody SignTopicRequest signTopicRequest
	) {
		signTopicService.updateSignTopic(topicId, signTopicRequest);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
}
