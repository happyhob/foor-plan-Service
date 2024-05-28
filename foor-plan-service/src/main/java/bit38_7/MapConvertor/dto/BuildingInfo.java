package bit38_7.MapConvertor.dto;


import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class BuildingInfo {
	String buildingName;
	int buildingCount;

	public BuildingInfo(String buildingName, int buildingCount) {
		this.buildingName = buildingName;
		this.buildingCount = buildingCount;
	}
}
