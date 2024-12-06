package site.sonisori.sonisori.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import site.sonisori.sonisori.common.constants.ErrorMessage;
import site.sonisori.sonisori.dto.signwordresource.SignWordResourcesRequest;
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
	public void addSignWordResource(
		Long signWordId,
		SignWordResourcesRequest resourcesRequest
	) {
		SignWord signWord = signWordRepository.findById(signWordId)
			.orElseThrow(() -> new NotFoundException(ErrorMessage.NOT_FOUND_WORD.getMessage()));

		resourcesRequest.resources().forEach(resourceRequest -> {
			SignWordResource resource = SignWordResource.builder()
				.signWord(signWord)
				.resourceType(resourceRequest.resourceType())
				.resourceUrl(resourceRequest.resourceUrl())
				.build();
			signWordResourceRepository.save(resource);
		});
	}
}
