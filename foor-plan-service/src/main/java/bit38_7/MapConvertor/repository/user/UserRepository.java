package bit38_7.MapConvertor.repository.user;

import bit38_7.MapConvertor.domain.user.User;

import bit38_7.MapConvertor.dto.UserRequest;
import java.util.Optional;

public interface UserRepository {
    User save(User user);
//    String checkId(String loginId);
    String findUserId(UserRequest findRequest);
	String findUserPw(UserRequest findRequest);
	Optional<User> login(String username, String password);
}
