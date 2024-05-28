package bit38_7.MapConvertor.dto;

import java.util.Map;
import lombok.Data;

@Data
public class BuildingRenderResponse {

	String buildingData;
	Map<Integer, String> floorData;
	Map<Integer, String> metaData;
}
