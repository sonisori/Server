package site.sonisori.sonisori.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import site.sonisori.sonisori.jwt.JwtFilter;
import site.sonisori.sonisori.jwt.JwtUtil;
import site.sonisori.sonisori.oauth2.CustomSuccessHandler;
import site.sonisori.sonisori.service.CustomOAuth2UserService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	private final CustomOAuth2UserService customOAuth2UserService;
	private final CustomSuccessHandler customSuccessHandler;
	private final JwtUtil jwtUtil;

	public SecurityConfig(CustomOAuth2UserService customOAuth2UserService, CustomSuccessHandler customSuccessHandler,
		JwtUtil jwtUtil) {
		this.customOAuth2UserService = customOAuth2UserService;
		this.customSuccessHandler = customSuccessHandler;
		this.jwtUtil = jwtUtil;
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		//csrf disable
		http
			.csrf((auth) -> auth.disable());

		//From 로그인 방식 disable
		http
			.formLogin((auth) -> auth.disable());

		//HTTP Basic 인증 방식 disable
		http
			.httpBasic((auth) -> auth.disable());

		//JWTFilter 추가*

		http
			.addFilterBefore(new JwtFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class);

		//oauth2
		http
			.oauth2Login((oauth2) -> oauth2
				.userInfoEndpoint((userInfoEndpointConfig) -> userInfoEndpointConfig
					.userService(customOAuth2UserService))
				.successHandler(customSuccessHandler));

		//경로별 인가 작업
		http
			.authorizeHttpRequests((auth) -> auth
				.requestMatchers("/").permitAll()
				.anyRequest().authenticated());

		//세션 설정 : STATELESS
		http
			.sessionManagement((session) -> session
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

		return http.build();
	}
}
