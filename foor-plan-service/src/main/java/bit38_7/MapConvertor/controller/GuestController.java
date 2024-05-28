package bit38_7.MapConvertor.controller;

import bit38_7.MapConvertor.dto.FloorInfo;
import bit38_7.MapConvertor.dto.ModelResponse;
import bit38_7.MapConvertor.service.FileService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class GuestController {

	private final FileService fileService;

	/**
	 *  건물정보 조회
	 * @param buildingId
	 * @return 층 리스트
	 */
	@GetMapping("guest/{buildingId}/list")
	public ResponseEntity<?> floorList(@PathVariable("buildingId") int buildingId) {

		List<FloorInfo> floorList = fileService.floorList(buildingId);
		log.info("floorList = {}", floorList);

		return ResponseEntity.ok().body(floorList);
	}


	/**
	 * 건물 선택시 파일 다운로드
	 * @return 건물 모델파일
	 */
	@GetMapping("guest/{buildingId}")
	public ResponseEntity<?> buildingDownload(@PathVariable("buildingId") int buildingId) {

		byte[] buildingData = fileService.buildingDownload(buildingId);

		return ResponseEntity.ok().body(buildingData);
	}


	/**
	 * 층 선택시 파일 다운로드
	 * @return 층 모델파일
	 */
	@GetMapping("guest/{buildingId}/{floorNum}")
	public ResponseEntity<?> floorDownload(@PathVariable("buildingId") int buildingId,
		@PathVariable("floorNum") int floorNum) {

		ModelResponse modelResponse = fileService.floorDownload(buildingId, floorNum);

		return ResponseEntity.ok().body(modelResponse);
	}

	@GetMapping("guest/{buildingId}/name")
	public ResponseEntity<?> buildingName(@PathVariable("buildingId") int buildingId) {
		String buildingName = fileService.buildingName(buildingId);
		return ResponseEntity.ok().body(buildingName);
	}

}

