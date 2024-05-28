package bit38_7.MapConvertor.controller;


import bit38_7.MapConvertor.domain.user.User;
import bit38_7.MapConvertor.dto.InfoRequest;
import bit38_7.MapConvertor.dto.UserRequest;
import bit38_7.MapConvertor.exception.MemberNotFoundException;
import bit38_7.MapConvertor.interceptor.session.SessionConst;
import bit38_7.MapConvertor.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RequiredArgsConstructor
@RestController

public class UserController {

	private final UserService userService;


	/**
	 * 유저 정보 조회
	 * @return loginId, userName
	 */

	@GetMapping("/users/info")
	public ResponseEntity<?> userInfo(HttpServletRequest request) {

		HttpSession session = request.getSession(false);
		User user = (User) session.getAttribute(SessionConst.LOGIN_MEMBER);

		InfoRequest responseUser = new InfoRequest();

		responseUser.setLoginId(user.getLoginId());
		responseUser.setUserName(user.getUserName());

		return ResponseEntity.ok().body(responseUser);
	}


	/**
	 * 아이디찾기
	 * @param userName
	 * @param email
	 * @return loginId
	 */
	@GetMapping("/users/id")
	public ResponseEntity<?> userId(@RequestParam("userName") String userName, @RequestParam("email") String email) {

		log.info("userName = {}, email = {} ", userName, email);

		UserRequest userRequest = new UserRequest();
		userRequest.setUserName(userName);
		userRequest.setEmail(email);

		String loginId = userService.findId(userRequest);
		log.info("아이디찾기 결과={}", loginId);

		if (loginId == null) {
			throw new MemberNotFoundException();
		}

		return ResponseEntity.ok().body(loginId);
	}

	/**
	 * 비밀번호찾기
	 *
	 * @param userName
	 * @param loginId
	 * @param email
	 * @return password
	 */
	@GetMapping("/users/pw")
	public ResponseEntity<?> userPw(@RequestParam("userName") String userName,
		@RequestParam("loginId") String loginId, @RequestParam("email") String email) {

		log.info("userName = {}, userId = {}, email = {} ", userName, loginId, email);

		UserRequest userRequest = new UserRequest();
		userRequest.setUserName(userName);
		userRequest.setLoginId(loginId);
		userRequest.setEmail(email);

		String password = userService.findPw(userRequest);
		log.info("비밀번호 찾기결과={}", password);

		if (password == null) {
			throw new MemberNotFoundException();
		}

		return ResponseEntity.ok().body(password);
	}

	@GetMapping("/users/session")
	public ResponseEntity<?> userSession(HttpServletRequest request) {

		HttpSession session = request.getSession(false);
		User user = (User) session.getAttribute(SessionConst.LOGIN_MEMBER);

		if (user == null) {
			return ResponseEntity.badRequest().body("세션이 없습니다.");
		}

		return ResponseEntity.ok().body(user.getUserId());
	}

}
