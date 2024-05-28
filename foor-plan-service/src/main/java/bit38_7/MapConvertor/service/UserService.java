package bit38_7.MapConvertor.service;

import bit38_7.MapConvertor.domain.user.User;
import bit38_7.MapConvertor.dto.UserRequest;
import bit38_7.MapConvertor.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User login(User user) {
        return userRepository.login(user.getLoginId(), user.getPassword())
                .orElse(null);
    }


    public User join(User user) {
        User save = userRepository.save(user);
        return save;
    }

    public String findId(UserRequest findRequest) {
        String userId = userRepository.findUserId(findRequest);
        return userId;
    }

    public String findPw(UserRequest findRequest) {
        String userPw = userRepository.findUserPw(findRequest);
        return userPw;
    }


//    private void validateDuplicateUser(User user) {
//        userRepository.findUserId(user.getLoginId())
//                .ifPresent(m -> {
//                    throw new IllegalStateException("이미 존재하는 회원입니다.");
//                });
//    }

}
