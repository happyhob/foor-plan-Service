package bit38_7.MapConvertor.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class BuildingResponse {

	int buildingId;
	String buildingName;
	int buildingFloor;
}
