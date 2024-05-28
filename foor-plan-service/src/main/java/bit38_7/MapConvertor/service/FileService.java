package bit38_7.MapConvertor.service;

import bit38_7.MapConvertor.dto.BuildingInfo;
import bit38_7.MapConvertor.dto.BuildingResponse;
import bit38_7.MapConvertor.dto.FloorInfo;
import bit38_7.MapConvertor.dto.ModelResponse;
import bit38_7.MapConvertor.repository.file.JdbcFileRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Service
@RequiredArgsConstructor
public class FileService {

	private final JdbcFileRepository jdbcFileRepository;

	/**
	 * 건물 저장
	 * @param userId 유저아이디
	 * @param buildingInfo 건물정보 (건물이름, 층수)
	 * @param fileData 건물사진
	 * @return 건물아이디
	 */
	public int buildingSave(int userId,BuildingInfo buildingInfo, byte[] fileData) {
		return jdbcFileRepository.saveBuilding(userId, buildingInfo.getBuildingName(), fileData, buildingInfo.getBuildingCount());
	}

	/**
	 * 층 저장
	 * @param buildingId 건물아이디
	 * @param floorNum 층번호
	 * @param floorData 층데이터
	 */
	public void floorSave(int buildingId,int floorNum ,byte[] floorData, byte[]jsonData) {
		jdbcFileRepository.saveFloor(buildingId, floorNum, floorData, jsonData);
	}

	/**
	 * 층 정보 수정
	 * @param floorNum 층 번호
	 * @param floorData 층 데이터
	 */
	public void floorUpdate(int buildingId, int floorNum, byte[] floorData, byte[] jsonData) {
		jdbcFileRepository.updateFloor(buildingId, floorNum, floorData, jsonData);
	}


	public void floorDelete(int buildingId,int floorNum) {
		jdbcFileRepository.deleteFloor(buildingId, floorNum);
	}


	/**
	 * 건물 리스트 조회
	 * @param userId 유저아이디
	 */
	public List<BuildingResponse> buildingList(int userId) {
		return jdbcFileRepository.userBuildingList(userId);
	}

	/**
	 * 건물 모델 다운로드
	 * @param buildingId 건물아이디
	 */
	public byte[] buildingDownload(int buildingId) {
		return jdbcFileRepository.findBuildingFile(buildingId);
	}

	public List<FloorInfo> floorList(int buildingId) {
		return jdbcFileRepository.findFloorList(buildingId);
	}

	public ModelResponse floorDownload(int buildingId, int floorNum) {
		return jdbcFileRepository.findFloorFile(buildingId, floorNum);
	}

	public void addPartFloor(int buildingId, int floorNum, byte[] floorData, byte[] jsonData) {
		jdbcFileRepository.updateFloor(buildingId, floorNum, floorData, jsonData);
	}

	@Transactional
	public void buildingDelete(int buildingId) {
		jdbcFileRepository.deleteFloorAll(buildingId);
		jdbcFileRepository.deleteBuilding(buildingId);
	}

	public String buildingName(int buildingId) {
		return jdbcFileRepository.findBuildingName(buildingId);
	}
}
