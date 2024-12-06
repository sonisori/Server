package site.sonisori.sonisori.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import site.sonisori.sonisori.common.constants.ErrorMessage;
import site.sonisori.sonisori.dto.signwordresource.SignWordResourceListRequest;
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
	public void addSignWordResources(
		Long signWordId,
		SignWordResourceListRequest resourcesRequest
	) {
		SignWord signWord = signWordRepository.findById(signWordId)
			.orElseThrow(() -> new NotFoundException(ErrorMessage.NOT_FOUND_WORD.getMessage()));

		List<SignWordResource> resources = resourcesRequest.resources().stream()
			.map(resourceRequest -> SignWordResource.builder()
				.signWord(signWord)
				.resourceType(resourceRequest.resourceType())
				.resourceUrl(resourceRequest.resourceUrl())
				.build())
			.collect(Collectors.toList());

		signWordResourceRepository.saveAll(resources);
	}
}
