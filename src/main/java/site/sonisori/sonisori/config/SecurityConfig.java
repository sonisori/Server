package site.sonisori.sonisori.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.RequiredArgsConstructor;
import site.sonisori.sonisori.jwt.JwtFilter;
import site.sonisori.sonisori.jwt.JwtUtil;
import site.sonisori.sonisori.oauth2.CustomSuccessHandler;
import site.sonisori.sonisori.service.CustomOAuth2UserService;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

	private final CustomOAuth2UserService customOAuth2UserService;
	private final CustomSuccessHandler customSuccessHandler;
	private final JwtUtil jwtUtil;

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

		http
			.csrf((auth) -> auth.disable());

		http
			.formLogin((auth) -> auth.disable());

		http
			.httpBasic((auth) -> auth.disable());

		http
			.addFilterBefore(new JwtFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class);

		http
			.oauth2Login((oauth2) -> oauth2
				.userInfoEndpoint((userInfoEndpointConfig) -> userInfoEndpointConfig
					.userService(customOAuth2UserService))
				.successHandler(customSuccessHandler));

		http
			.authorizeHttpRequests((auth) -> auth
				.requestMatchers("/").permitAll()
				.anyRequest().authenticated());

		http
			.sessionManagement((session) -> session
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

		return http.build();
	}
}
