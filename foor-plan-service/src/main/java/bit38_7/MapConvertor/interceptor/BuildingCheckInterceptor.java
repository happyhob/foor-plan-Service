package bit38_7.MapConvertor.interceptor;

import bit38_7.MapConvertor.domain.user.User;
import bit38_7.MapConvertor.interceptor.session.SessionConst;
import bit38_7.MapConvertor.repository.file.JdbcFileRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;

@Slf4j
public class BuildingCheckInterceptor implements HandlerInterceptor {

    private final JdbcFileRepository jdbcFileRepository;

    public BuildingCheckInterceptor(JdbcFileRepository jdbcFileRepository) {
        this.jdbcFileRepository = jdbcFileRepository;
    }


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
        throws IOException {

        HttpSession session = request.getSession(false);
        User user = (User)session.getAttribute(SessionConst.LOGIN_MEMBER);
        int userId = user.getUserId().intValue();


        final Map<String, String> pathVariables = (Map<String, String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        final int buildingId = Integer.parseInt(pathVariables.get("buildingId"));

        int results = jdbcFileRepository.findById(userId,buildingId);

        if (results == 0) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }
        return true;
    }

}
