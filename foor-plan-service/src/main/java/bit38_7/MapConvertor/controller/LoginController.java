package bit38_7.MapConvertor.controller;

import bit38_7.MapConvertor.domain.user.User;
import bit38_7.MapConvertor.dto.LoginRequest;
import bit38_7.MapConvertor.exception.LoginFailureException;
import bit38_7.MapConvertor.interceptor.session.SessionConst;
import bit38_7.MapConvertor.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class LoginController {

	private final UserService userService;

	@PostMapping("/login")
	public ResponseEntity<?> login(HttpServletRequest request, @RequestBody LoginRequest loginRequest)
	{
		User user = new User();
		user.setLoginId(loginRequest.getLoginId());
		user.setPassword(loginRequest.getPassword());

		log.info("로그인 요청 정보={}", user);

		User loginResult = userService.login(user);

		if (loginResult == null) {
			throw new LoginFailureException();
		}

		// 로그인 성공 처리
		// HttpSession 사용해서 세션처리

		// 세션이 있으면 있는 세션 반환, 없으면 신규 세션을 생성
		HttpSession session = request.getSession(true);
		// 세션에 로그인 회원 정보 보관
		session.setAttribute(SessionConst.LOGIN_MEMBER, loginResult);

		log.info("session = {}", session.getAttribute(SessionConst.LOGIN_MEMBER));

		return ResponseEntity.ok().body("로그인 성공");
	}


	@PostMapping("/logout")
	public ResponseEntity<?> logout(HttpServletRequest request) {
		HttpSession session = request.getSession(false);

		if (session != null) {
			session.invalidate();
			return ResponseEntity.ok().body("로그아웃");

		}
		return ResponseEntity.ok().body("로그아웃");
	}



	@PostMapping("/join")
	public ResponseEntity<?> registerUser(@Valid @RequestBody User user, BindingResult bindingResult) {

		if (bindingResult.hasErrors()) {
			Map<String, String> errors = new HashMap<>();
			for (FieldError error : bindingResult.getFieldErrors()) {
				errors.put(error.getField(), error.getDefaultMessage());
			}
			return ResponseEntity.badRequest().body(errors);
		}

		log.info("회원가입 요청 정보={}", user);

		User joinResult = userService.join(user);
		log.info("회원가입 결과 정보={}", joinResult);

		if (joinResult == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(Collections.singletonMap("결과", "회원가입 실패"));
		}


		return ResponseEntity.ok(Collections.singletonMap("결과", "회원가입 성공"));
	}
}
