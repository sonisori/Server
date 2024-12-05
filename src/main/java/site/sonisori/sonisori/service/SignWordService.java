package site.sonisori.sonisori.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
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
			.map(SignWord::toDTO)
			.collect(Collectors.toList());
	}
}
