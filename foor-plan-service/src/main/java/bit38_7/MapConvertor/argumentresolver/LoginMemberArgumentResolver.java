package bit38_7.MapConvertor.argumentresolver;

import bit38_7.MapConvertor.domain.user.User;
import bit38_7.MapConvertor.interceptor.session.SessionConst;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Slf4j
public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		log.info("supportsParameter 실행");

		boolean hasLoginAnnotation = parameter.hasParameterAnnotation(Login.class);				// 어노테이션이 Login이고
		boolean hasMemberType = User.class.isAssignableFrom(parameter.getParameterType());		// 파라미터 타입이 User일때

		return hasLoginAnnotation && hasMemberType;		// 조건이 참이면 해당 리졸버로 작업 수행
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
		log.info("resolveArgument 실행");

		// HttpServletRequest 프록시 객체가 아니라 진짜 객체를 꺼내 요청정보를 확인
		HttpServletRequest request = (HttpServletRequest)webRequest.getNativeRequest();

		HttpSession session = request.getSession(false);
		if (session == null) {
			return null;
		}

 		return session.getAttribute(SessionConst.LOGIN_MEMBER);
	}
}
