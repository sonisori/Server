package site.sonisori.sonisori.dto.user;

import lombok.Builder;

@Builder
public record AuthResponse(
	boolean isLogin,
	String name
) {
}
