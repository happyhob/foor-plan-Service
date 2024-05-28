package bit38_7.MapConvertor.repository.file;


import bit38_7.MapConvertor.dto.BuildingResponse;
import java.util.List;
import javax.sql.DataSource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
class JdbcFileRepositoryTest {

	@Autowired
	private final NamedParameterJdbcTemplate template;

	public JdbcFileRepositoryTest(DataSource dataSource) {
		this.template = new NamedParameterJdbcTemplate(dataSource);
	}




	public List<BuildingResponse> userBuildingList(int userId) {
		String sql = "select building_id, building_name, building_facade, building_floor from building_table where fk_user_id = :userId";
		try {
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("userId", userId);

			return template.query(sql, params, buildingRowMapper());
		} catch (DataAccessException e) {
			return null;
		}
	}


	@Test
	void findUserBuildingList() {
		List<BuildingResponse> buildingResponses = userBuildingList(1);
		buildingResponses.forEach(System.out::println);
	}

	private RowMapper<BuildingResponse> buildingRowMapper() {
		return BeanPropertyRowMapper.newInstance(BuildingResponse.class);
	}

	@Test
	void deletebuilding(int buildingId) {
		String sql = "delete from building_table where building_id = :buildingId";
		try {
			MapSqlParameterSource params = new MapSqlParameterSource();
			params.addValue("buildingId", buildingId);

			template.update(sql, params);
		} catch (DataAccessException e) {
			System.out.println(e.getMessage());
		}
	}

}