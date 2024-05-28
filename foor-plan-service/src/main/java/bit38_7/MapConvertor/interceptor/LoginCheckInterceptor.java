package bit38_7.MapConvertor.interceptor;


import bit38_7.MapConvertor.interceptor.session.SessionConst;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
public class LoginCheckInterceptor implements HandlerInterceptor{

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

		HttpSession session = request.getSession(false);

		if (session == null || session.getAttribute(SessionConst.LOGIN_MEMBER) == null) {
			log.info("미인증 사용자 요청");

			response.sendError(HttpServletResponse.SC_UNAUTHORIZED);

			return false;
		}

		return true;
	}
}
