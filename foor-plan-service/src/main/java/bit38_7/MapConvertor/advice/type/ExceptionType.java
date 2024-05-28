package bit38_7.MapConvertor.advice.type;

import lombok.Getter;

@Getter
public enum ExceptionType {
    LOGIN_FAILURE_EXCEPTION("loginFailureException.code", "loginFailureException.msg"),
    MEMBER_NOT_FOUND_EXCEPTION("memberNotFoundException.code", "memberNotFoundException.msg");

    private final String code;
    private final String message;
    ExceptionType(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
