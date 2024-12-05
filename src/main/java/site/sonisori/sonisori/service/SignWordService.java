package site.sonisori.sonisori.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import site.sonisori.sonisori.common.response.SuccessResponse;
import site.sonisori.sonisori.dto.signword.SignWordRequest;
import site.sonisori.sonisori.dto.signword.SignWordResponse;
import site.sonisori.sonisori.entity.SignWord;
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

	@Transactional
	public SuccessResponse addSignWord(SignWordRequest signWordRequest) {
		SignWord signWord = SignWord.builder()
			.word(signWordRequest.word())
			.description(signWordRequest.description())
			.build();

		Long id = signWordRepository.save(signWord).getId();
		return new SuccessResponse(id);
	}
}
