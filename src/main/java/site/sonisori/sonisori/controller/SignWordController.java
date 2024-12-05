package site.sonisori.sonisori.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
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
}
