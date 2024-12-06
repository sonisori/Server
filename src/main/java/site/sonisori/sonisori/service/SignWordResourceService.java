package site.sonisori.sonisori.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import site.sonisori.sonisori.common.response.SuccessResponse;
import site.sonisori.sonisori.dto.signwordresource.SignWordResourceRequest;
import site.sonisori.sonisori.entity.SignWordResource;
import site.sonisori.sonisori.repository.SignWordResourceRepository;

@Service
@RequiredArgsConstructor
public class SignWordResourceService {
	private final SignWordResourceRepository signWordResourceRepository;

	@Transactional
	public SuccessResponse addSignWordResource(SignWordResourceRequest resourceRequest) {
		SignWordResource resource = SignWordResource.builder()
			.resourceType(resourceRequest.resourceType())
			.resourceUrl(resourceRequest.resourceUrl())
			.build();
		Long id = signWordResourceRepository.save(resource).getId();
		return new SuccessResponse(id);
	}
}
