package site.sonisori.sonisori.dto.signword;

import java.util.List;

import site.sonisori.sonisori.dto.signwordresource.SignWordResourceDto;

public record SignWordDetailResponse(
	String word,
	String description,
	List<SignWordResourceDto> resources
) {
}
