package site.sonisori.sonisori.auth.jwt;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.UUID;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import site.sonisori.sonisori.auth.jwt.dto.TokenDto;
import site.sonisori.sonisori.auth.jwt.entity.RefreshToken;
import site.sonisori.sonisori.auth.jwt.repository.RefreshTokenRepository;
import site.sonisori.sonisori.auth.oauth2.CustomOAuth2Service;
import site.sonisori.sonisori.common.constants.ErrorMessage;
import site.sonisori.sonisori.entity.User;
import site.sonisori.sonisori.exception.NotFoundException;
import site.sonisori.sonisori.repository.UserRepository;

@Component
public class JwtUtil {
	private static final int MILLIS = 1000;
	private final RefreshTokenRepository refreshTokenRepository;
	private final String issuer;
	private final long accessTokenExpiration;
	private final long refreshTokenExpiration;
	private final SecretKey secretKey;
	private final CustomOAuth2Service customOAuth2Service;
	private final UserRepository userRepository;

	public JwtUtil(RefreshTokenRepository refreshTokenRepository,
		CustomOAuth2Service customOAuth2Service,
		@Value("${spring.jwt.secret-key}") String secret,
		@Value("${spring.application.name}") String issuer,
		@Value("${spring.jwt.access-expiration}") long accessTokenExpiration,
		@Value("${spring.jwt.refresh-expiration}") long refreshTokenExpiration, UserRepository userRepository) {
		this.refreshTokenRepository = refreshTokenRepository;
		this.issuer = issuer;
		this.accessTokenExpiration = accessTokenExpiration * MILLIS;
		this.refreshTokenExpiration = refreshTokenExpiration * MILLIS;
		this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
		this.customOAuth2Service = customOAuth2Service;
		this.userRepository = userRepository;
	}

	public String createAccessToken(User user) {
		Date now = new Date();
		Date expiryDate = new Date(now.getTime() + accessTokenExpiration);

		return Jwts.builder()
			.issuer(issuer)
			.issuedAt(now)
			.subject(String.valueOf(user.getId()))
			.claim("token_type", "access_token")
			.claim("role", user.getRole())
			.claim("username", user.getUsername())
			.expiration(expiryDate)
			.signWith(secretKey)
			.compact();
	}

	public String createRefreshToken(User user) {
		RefreshToken refreshToken = new RefreshToken(UUID.randomUUID().toString(), user.getId());
		refreshTokenRepository.save(refreshToken);
		return refreshToken.getRefreshToken();
	}

	public void deleteRefreshToken(String refreshToken, Long userId) {
		validateRefreshToken(refreshToken, userId);

		refreshTokenRepository.deleteById(refreshToken);
	}

	public TokenDto generateJwt(User user) {
		String accessToken = createAccessToken(user);
		String refreshToken = createRefreshToken(user);

		long currentMillis = System.currentTimeMillis();

		return TokenDto.builder()
			.accessToken(accessToken)
			.accessTokenExpiresIn(currentMillis + accessTokenExpiration)
			.refreshToken(refreshToken)
			.refreshTokenExpiresIn(currentMillis + refreshTokenExpiration)
			.build();
	}

	public Claims extractClaims(String accessToken) {
		try {
			return Jwts.parser().verifyWith(secretKey).build()
				.parseSignedClaims(accessToken).getPayload();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public boolean validateAccessToken(String accessToken) {
		try {
			Claims claims = extractClaims(accessToken);
			if (!claims.get("token_type").equals("access_token") || accessToken == null) {
				throw new JwtException(ErrorMessage.NOT_FOUND_TOKEN.getMessage());
			}
			return !claims.getExpiration().before(new Date());
		} catch (ExpiredJwtException e) {
			throw new JwtException(ErrorMessage.NOT_FOUND_TOKEN.getMessage());
		} catch (SignatureException | MalformedJwtException | UnsupportedJwtException | IllegalArgumentException e) {
			throw new JwtException(ErrorMessage.INVALID_TOKEN.getMessage());
		} catch (JwtException e) {
			throw new JwtException(e.getMessage());
		}
	}

	public void validateRefreshToken(String refreshToken, Long userId) {
		RefreshToken token = refreshTokenRepository.findById(refreshToken)
			.orElseThrow(() -> new JwtException(ErrorMessage.NOT_FOUND_TOKEN.getMessage()));
		if (!(token.getUserId().equals(userId))) {
			throw new JwtException(ErrorMessage.INVALID_TOKEN.getMessage());
		}
	}

	public String getUsername(String token) {
		Claims claims = extractClaims(token);
		return claims.get("username", String.class);
	}

	public Authentication getAuthentication(String token) {
		UserDetails userDetails = customOAuth2Service.loadUserByUsername(getUsername(token));
		return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
	}

	public String reissueAccessToken(String refreshToken, Long userId) {
		validateRefreshToken(refreshToken, userId);
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new NotFoundException(ErrorMessage.NOT_FOUND_USER.getMessage()));

		return createAccessToken(user);
	}
}
