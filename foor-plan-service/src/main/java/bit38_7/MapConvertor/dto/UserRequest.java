package bit38_7.MapConvertor.dto;


import jakarta.validation.Valid;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Valid
@RequiredArgsConstructor
public class UserRequest {
	private String loginId;
	private String password;
	private String userName;
	private String email;
}
