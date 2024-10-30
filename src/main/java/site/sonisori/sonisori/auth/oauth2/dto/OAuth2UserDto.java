package site.sonisori.sonisori.auth.oauth2.dto;

public record OAuth2UserDto(
	String name,
	String username,
	String role,
	String email
) {
}
