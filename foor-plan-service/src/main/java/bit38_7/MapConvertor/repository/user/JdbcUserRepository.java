package bit38_7.MapConvertor.repository.user;

import bit38_7.MapConvertor.domain.user.User;
import bit38_7.MapConvertor.dto.UserRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import javax.sql.DataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;


@Slf4j
@Repository
public class JdbcUserRepository implements UserRepository {


	private final NamedParameterJdbcTemplate template;
	public JdbcUserRepository(DataSource dataSource) {
		this.template = new NamedParameterJdbcTemplate(dataSource);
	}

	@Override
	public User save(User user) {
		String sql = "insert into users(login_id, user_name, password, email) values(:loginId, :userName, :password, :email)";

		try {
//			String result = checkId(user.getLoginId());
//			if (result == null) {
//				return null;
//			}

			SqlParameterSource param = new BeanPropertySqlParameterSource(user);

			KeyHolder keyHolder = new GeneratedKeyHolder();
			template.update(sql, param, keyHolder);

			long key = keyHolder.getKey().longValue();

			user.setUserId(key);

			return user;
		}catch (DataAccessException e){
			log.info("error = {}", e.getMessage());
			return null;
		}
	}

	@Override
	public Optional<User> login(String loginId, String password) {
		String sql = "select * from users where login_id = :loginId and password = :password";
		try {
			Map<String, Object> params = new HashMap<>();
			params.put("loginId", loginId);
			params.put("password", password);
			User user = template.queryForObject(sql, params, userRowMapper());
			return Optional.of(user);
		} catch (EmptyResultDataAccessException e) {
			return Optional.empty();
		}
	}

//	//추가된 부분
//	@Override
//	public String checkId(String loginId) {
//		String sql = "select login_id from users where login_id = :loginId";
//
//		try{
//			log.info("findParam = {}", loginId);
//			SqlParameterSource param = new BeanPropertySqlParameterSource(loginId);
//			User user = template.queryForObject(sql, param, userRowMapper());
//			log.info("user = {}", user);
//			return user.getLoginId();
//		}
//		catch (EmptyResultDataAccessException e) {
//			return null;
//		}
//	}


	@Override
	public String findUserId(UserRequest findParam) {
		String sql = "select login_id from users where user_name = :userName and email = :email";

		try{
			log.info("findParam = {}", findParam);
			SqlParameterSource param = new BeanPropertySqlParameterSource(findParam);
			User user = template.queryForObject(sql, param, userRowMapper());
			log.info("user = {}", user);
			return user.getLoginId();
		}
		catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	@Override
	public String findUserPw(UserRequest findParam) {
		String sql = "select password from users where user_name = :userName and login_id = :loginId and email = :email";

		try{
			log.info("findRequest = {}", findParam);
			SqlParameterSource param = new BeanPropertySqlParameterSource(findParam);
			User user = template.queryForObject(sql, param, userRowMapper());
			log.info("user = {}", user);
			return user.getPassword();
		}
		catch (EmptyResultDataAccessException e) {
			return null;
		}
	}


	private RowMapper<User> userRowMapper(){
		return BeanPropertyRowMapper.newInstance(User.class);
	}
}
