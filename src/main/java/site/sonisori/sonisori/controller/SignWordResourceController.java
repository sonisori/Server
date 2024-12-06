package site.sonisori.sonisori.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import site.sonisori.sonisori.common.response.SuccessResponse;
import site.sonisori.sonisori.dto.signwordresource.SignWordResourceRequest;
import site.sonisori.sonisori.service.SignWordResourceService;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class SignWordResourceController {
	private final SignWordResourceService signWordResourceService;

	@PostMapping("/admin/words/{wordId}/resources")
	public ResponseEntity<SuccessResponse> addSignWordResourceByAdmin(
		@PathVariable(name = "wordId") Long wordId,
		@RequestBody @Valid SignWordResourceRequest resourceRequest
	) {
		SuccessResponse successResponse = signWordResourceService.addSignWordResource(
			wordId, resourceRequest
		);
		return ResponseEntity.status(HttpStatus.CREATED).body(successResponse);
	}
}
