package bit38_7.MapConvertor.dto;

import jakarta.validation.Valid;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@Valid
public class LoginRequest {

	private String loginId;
	private String password;
}
