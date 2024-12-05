package site.sonisori.sonisori.dto.signword;

import lombok.Builder;

@Builder
public record SignWordResponse(
	Long id,
	String word
) {
}
