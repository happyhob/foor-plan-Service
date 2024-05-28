package bit38_7.MapConvertor.domain.user;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;


@Data
@Valid
public class User {

	// 서버에 저장되는 유저id
	private Long userId;


	@NotBlank(message = "아이디를 입력 해주세요")
	@Size(min = 3, message = "아이디 는 3자 이상")
	private String loginId;

	@NotBlank(message = "비밀번호를 입력 해주세요")
	@Size(min = 3, message = "비밀번호는 3자 이상")
	private String password;

	@NotBlank(message = "이름를 입력해주세요")
	@Size(min = 3,message = "닉네임은 3자 이삼")
	private String userName;

	@Email(message = "이메일 형식에 맞지 않아요")
	private String email;

	public User() {
	}
}
