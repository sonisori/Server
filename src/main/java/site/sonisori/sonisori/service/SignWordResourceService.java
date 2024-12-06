package site.sonisori.sonisori.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import site.sonisori.sonisori.common.constants.ErrorMessage;
import site.sonisori.sonisori.common.response.SuccessResponse;
import site.sonisori.sonisori.dto.signwordresource.SignWordResourceRequest;
import site.sonisori.sonisori.entity.SignWord;
import site.sonisori.sonisori.entity.SignWordResource;
import site.sonisori.sonisori.exception.NotFoundException;
import site.sonisori.sonisori.repository.SignWordRepository;
import site.sonisori.sonisori.repository.SignWordResourceRepository;

@Service
@RequiredArgsConstructor
public class SignWordResourceService {
	private final SignWordResourceRepository signWordResourceRepository;
	private final SignWordRepository signWordRepository;

	@Transactional
	public SuccessResponse addSignWordResource(
		Long signWordId,
		SignWordResourceRequest resourceRequest
	) {
		SignWord signWord = signWordRepository.findById(signWordId)
			.orElseThrow(() -> new NotFoundException(ErrorMessage.NOT_FOUND_WORD.getMessage()));
		SignWordResource resource = SignWordResource.builder()
			.signWord(signWord)
			.resourceType(resourceRequest.resourceType())
			.resourceUrl(resourceRequest.resourceUrl())
			.build();

		Long id = signWordResourceRepository.save(resource).getId();
		return new SuccessResponse(id);
	}
}
