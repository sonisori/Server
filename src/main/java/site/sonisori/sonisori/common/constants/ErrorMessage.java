package site.sonisori.sonisori.common.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorMessage {
	INVALID_REQUEST("유효하지 않은 요청입니다."),
	SERVER_ERROR("서버 오류가 발생했습니다."),
	EXPIRED_TOKEN("토큰이 만료되었습니다."),
	NOT_FOUND_TOKEN("토큰이 존재하지 않습니다."),
	INVALID_TOKEN("유효하지 않은 토큰입니다."),
	METHOD_VALIDATION_FAILED("메서드 유효성 검사에 실패했습니다."),
	NULL_POINTER_EXCEPTION("서버에서 null 참조 오류가 발생했습니다."),
	DUPLICATE_EMAIL("이미 사용 중인 이메일입니다."),
	INVALID_USER("회원 정보가 잘못되었습니다."),
	NOT_FOUND_TOPIC("존재하지 않는 토픽입니다.");

	public static final String INVALID_VALUE = "형식에 올바르게 작성해주세요.";

	private final String message;
}
