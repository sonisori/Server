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
import site.sonisori.sonisori.dto.signwordresource.SignWordResourceListRequest;
import site.sonisori.sonisori.service.SignWordResourceService;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class SignWordResourceController {
	private final SignWordResourceService signWordResourceService;

	@PostMapping("/admin/words/{wordId}/resources")
	public ResponseEntity<Void> addSignWordResourcesByAdmin(
		@PathVariable(name = "wordId") Long wordId,
		@RequestBody @Valid SignWordResourceListRequest resourcesRequest
	) {
		signWordResourceService.addSignWordResources(
			wordId, resourcesRequest
		);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
}
