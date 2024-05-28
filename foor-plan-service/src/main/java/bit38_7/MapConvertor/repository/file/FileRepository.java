package bit38_7.MapConvertor.repository.file;


import bit38_7.MapConvertor.dto.BuildingResponse;
import bit38_7.MapConvertor.dto.FloorInfo;
import bit38_7.MapConvertor.dto.ModelResponse;
import java.util.List;

public interface FileRepository {

	int saveBuilding(int userId, String buildingName, byte[] buildingFacade, int buildingCount);

	void saveFloor(int buildingId, int floorNum, byte[] floorData, byte[] jsonData);

	List<BuildingResponse> userBuildingList(int userId);

	byte[] findBuildingFile(int buildingId);

	List<FloorInfo> findFloorList(int buildingId);

	ModelResponse findFloorFile(int buildingId, int floorNum);

	void updateFloor(int buildingId,int floorNum, byte[] floorData, byte[] jsonData);

	void deleteFloor(int buildingId,int floorNum);

	int findById(int userId, int buildingId);

	void deleteBuilding(int buildingId);

	void deleteFloorAll(int buildingId);

	String findBuildingName(int buildingId);
}
