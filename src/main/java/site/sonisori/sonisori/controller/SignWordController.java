package site.sonisori.sonisori.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import site.sonisori.sonisori.common.response.SuccessResponse;
import site.sonisori.sonisori.dto.signword.SignWordRequest;
import site.sonisori.sonisori.dto.signword.SignWordResponse;
import site.sonisori.sonisori.service.SignWordService;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class SignWordController {
	private final SignWordService signWordService;

	@GetMapping("/words")
	public ResponseEntity<List<SignWordResponse>> getAllSignWords() {
		List<SignWordResponse> signWords = signWordService.getAllSignWords();
		return ResponseEntity.ok(signWords);
	}

	@PostMapping("/admin/words")
	public ResponseEntity<SuccessResponse> addSignWordByAdmin(@RequestBody @Valid SignWordRequest signWordRequest) {
		SuccessResponse successResponse = signWordService.addSignWord(signWordRequest);
		return ResponseEntity.status(HttpStatus.CREATED).body(successResponse);
	}

	@DeleteMapping("/admin/words/{wordId}")
	public ResponseEntity<Void> deleteSignWordByAdmin(
		@PathVariable(name = "wordId") Long wordId
	) {
		signWordService.deleteSignWord(wordId);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

	@PatchMapping("/admin/words/{wordId}")
	public ResponseEntity<Void> updateSignWordByAdmin(@Valid @RequestBody SignWordRequest signWordRequest,
		@PathVariable(name = "wordId") Long wordId
	) {
		signWordService.updateSignWord(wordId, signWordRequest);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
}
