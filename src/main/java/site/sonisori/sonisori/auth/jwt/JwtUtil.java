package site.sonisori.sonisori.auth.jwt;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.UUID;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
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
import site.sonisori.sonisori.common.constants.ErrorMessage;
import site.sonisori.sonisori.entity.User;

@Component
public class JwtUtil {
	private static final int MILLIS = 1000;
	private final RefreshTokenRepository refreshTokenRepository;
	private final String issuer;
	private final long accessTokenExpiration;
	private final long refreshTokenExpiration;
	private final SecretKey secretKey;

	public JwtUtil(RefreshTokenRepository refreshTokenRepository,
		@Value("${spring.jwt.secret-key}") String secret,
		@Value("${spring.application.name}") String issuer,
		@Value("${spring.jwt.access-expiration}") long accessTokenExpiration,
		@Value("${spring.jwt.refresh-expiration}") long refreshTokenExpiration) {
		this.refreshTokenRepository = refreshTokenRepository;
		this.issuer = issuer;
		this.accessTokenExpiration = accessTokenExpiration * MILLIS;
		this.refreshTokenExpiration = refreshTokenExpiration * MILLIS;
		this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
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
			.expiration(expiryDate)
			.signWith(secretKey)
			.compact();
	}

	public String createRefreshToken(User user) {
		RefreshToken refreshToken = new RefreshToken(UUID.randomUUID().toString(), user.getId());
		refreshTokenRepository.save(refreshToken);
		return refreshToken.getRefreshToken();
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
}
