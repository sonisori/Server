package site.sonisori.sonisori.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import site.sonisori.sonisori.common.constants.ErrorMessage;
import site.sonisori.sonisori.common.response.SuccessResponse;
import site.sonisori.sonisori.dto.signword.SignWordDetailResponse;
import site.sonisori.sonisori.dto.signword.SignWordRequest;
import site.sonisori.sonisori.dto.signword.SignWordResponse;
import site.sonisori.sonisori.dto.signwordresource.SignWordResourceDto;
import site.sonisori.sonisori.entity.SignWord;
import site.sonisori.sonisori.entity.SignWordResource;
import site.sonisori.sonisori.exception.NotFoundException;
import site.sonisori.sonisori.repository.SignWordRepository;

@Service
@RequiredArgsConstructor
public class SignWordService {
	private final SignWordRepository signWordRepository;

	public List<SignWordResponse> getAllSignWords() {
		List<SignWord> signWords = signWordRepository.findAll();
		return signWords.stream()
			.map(SignWord::toDto)
			.collect(Collectors.toList());
	}

	@Transactional(readOnly = true)
	public SignWordDetailResponse getSignWordDetail(Long wordId) {
		SignWord signWord = signWordRepository.findById(wordId)
			.orElseThrow(() -> new NotFoundException(ErrorMessage.NOT_FOUND_WORD.getMessage()));

		List<SignWordResourceDto> resources = mapSignWordResources(signWord.getSignWordResources());

		return new SignWordDetailResponse(
			signWord.getWord(),
			signWord.getDescription(),
			resources
		);
	}

	private List<SignWordResourceDto> mapSignWordResources(List<SignWordResource> resources) {
		return resources.stream()
			.map(resource -> new SignWordResourceDto(resource.getResourceType(), resource.getResourceUrl()))
			.toList();
	}

	@Transactional
	public SuccessResponse addSignWord(SignWordRequest signWordRequest) {
		SignWord signWord = SignWord.builder()
			.word(signWordRequest.word())
			.description(signWordRequest.description())
			.build();

		Long id = signWordRepository.save(signWord).getId();
		return new SuccessResponse(id);
	}

	public void deleteSignWord(Long wordId) {
		if (!signWordRepository.existsById(wordId)) {
			throw new NotFoundException(ErrorMessage.NOT_FOUND_WORD.getMessage());
		}

		signWordRepository.deleteById(wordId);
	}
}
