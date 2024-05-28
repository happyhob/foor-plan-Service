package bit38_7.MapConvertor.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;


@Slf4j
public class LogInterceptor implements HandlerInterceptor{

	public static final String LOG_ID = "logId";

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

		String requestURI = request.getRequestURI();
		String uuid = UUID.randomUUID().toString();

		request.setAttribute(LOG_ID, uuid);

		log.info("REQUEST [{}] [{}] [{}] [{}]", request.getMethod() , uuid, requestURI, handler);

		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
		log.info("postHandle [{}]", handler);
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {

		String requestURI = request.getRequestURI();
		Object uuid = request.getAttribute(LOG_ID);

		log.info("RESPONSE [{}] [{}] [{}]", uuid, requestURI, handler);
		if (ex != null) {
			log.error("afterCompletion error!! ", ex);
		}
	}
}
