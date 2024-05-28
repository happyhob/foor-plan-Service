package bit38_7.MapConvertor.controller;

import bit38_7.MapConvertor.domain.user.User;
import bit38_7.MapConvertor.dto.BuildingInfo;
import bit38_7.MapConvertor.dto.BuildingRenderResponse;
import bit38_7.MapConvertor.dto.BuildingResponse;
import bit38_7.MapConvertor.dto.FloorInfo;
import bit38_7.MapConvertor.dto.ModelResponse;
import bit38_7.MapConvertor.dto.floorRenderResponse;
import bit38_7.MapConvertor.interceptor.session.SessionConst;
import bit38_7.MapConvertor.service.FileService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequiredArgsConstructor
public class FileController {

	public static final String RENDER_SERVER = "http://10.101.68.13:7080/model";	// 렌더링 서버주소
	private final FileService fileService;

	@PostMapping("file")
	public ResponseEntity<?> fileSave(@RequestParam("buildingName") String buildingName,
									@RequestParam("floorCount") int floorCount,
									@RequestPart("files") List<MultipartFile> floors,
									HttpServletRequest request) throws IOException {

		BuildingInfo buildingInfo = new BuildingInfo(buildingName, floorCount);
		int userId = getUserId(request);

		String url = RENDER_SERVER;

		MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
		for (MultipartFile floor : floors) {
			ByteArrayResource resource = new ByteArrayResource(floor.getBytes()) {
				public String getFilename() {return floor.getOriginalFilename();}
			};
			params.add("floors", resource);
		}

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);

		HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<>(params, headers);

		ResponseEntity<BuildingRenderResponse> response = getBuildingRenderResponse(url, entity);
		BuildingRenderResponse responseBody = response.getBody();

		int buildingId = fileService.buildingSave(userId, buildingInfo, getDecodeByte(responseBody.getBuildingData()));


		Map<Integer, String> floorDataMap = responseBody.getFloorData();
		Map<Integer, String> floorJsonData = responseBody.getMetaData();

		for(Integer floorKey : floorDataMap.keySet()) {
			fileService.floorSave(buildingId, floorKey,
				getDecodeByte(floorDataMap.get(floorKey)),
				getDecodeByte(floorJsonData.get(floorKey)));


			// TODO 건물파일 생성 후 잘 받는지 로그찍어본 것 확인 후 지우기
			log.info("floorDataMap.get(floorKey) = {}", floorDataMap.get(floorKey));
			log.info("floorJsonData.get(floorKey) = {}", floorJsonData.get(floorKey));
		}

		return ResponseEntity.ok().body("저장 성공");
	}


	@PostMapping("file/{buildingId}/{floorNum}")
	public ResponseEntity<?> addPartFloor(@PathVariable("buildingId") int buildingId,
											@PathVariable("floorNum") int floorNum,
											@RequestParam("updateFile") MultipartFile updateFile) throws IOException {

		String url = RENDER_SERVER + "/addPartial";

		MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
		ByteArrayResource resource = new ByteArrayResource(updateFile.getBytes()) {
			public String getFilename() {
				return updateFile.getOriginalFilename();
			}
		};

		params.add("floor", resource);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);

		HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<>(params, headers);

		RestTemplate rt = new RestTemplate();
		ResponseEntity<floorRenderResponse> response = rt.exchange(
			url,
			HttpMethod.POST,
			entity,
			floorRenderResponse.class
		);
		byte[] floorBytes = getDecodeByte(response.getBody().getFloorData());
		byte[] floorMetaBytes = getDecodeByte(response.getBody().getMetaData());

		fileService.addPartFloor(buildingId, floorNum, floorBytes, floorMetaBytes);

		return ResponseEntity.ok().body("수정 성공");
	}



	/**
	 * 유저의 건물 리스트 조회
	 * @return 유저가 생성한 건물 리스트
	 */
	@GetMapping("file/list")
	public ResponseEntity<?> BuildingList(HttpServletRequest request) {

		int userId = getUserId(request);
		log.info("userId = {}", userId);

		List<BuildingResponse> buildingList = fileService.buildingList(userId);
		log.info("buildingList = {}", buildingList);

		return ResponseEntity.ok().body(buildingList);
	}

	/**
	 * 건물의 층 리스트 조회
	 * @param buildingId
	 * @return 층 리스트
	 */
	@GetMapping("file/{buildingId}/list")
	public ResponseEntity<?> floorList(@PathVariable("buildingId") int buildingId) {

		List<FloorInfo> floorList = fileService.floorList(buildingId);
		log.info("floorList = {}", floorList);

		return ResponseEntity.ok().body(floorList);
	}


	/**
	 * 건물 선택시 건물모델 다운로드
	 * @return 건물 모델파일
	 */
	@GetMapping("file/{buildingId}")
	public ResponseEntity<?> buildingDownload(@PathVariable("buildingId") int buildingId) {

		byte[] buildingData = fileService.buildingDownload(buildingId);

		return ResponseEntity.ok().body(buildingData);
	}

	/**
	 * 층 선택시 파일 다운로드
	 * @return 층 모델파일
	 */
	@GetMapping("file/{buildingId}/{floorNum}")
	public ResponseEntity<?> floorDownload(@PathVariable("buildingId") int buildingId,
											@PathVariable("floorNum") int floorNum) {

		ModelResponse modelResponse = fileService.floorDownload(buildingId, floorNum);

		return ResponseEntity.ok().body(modelResponse);
	}

	/**
	 * 층 정보 수정
	 * @param buildingId
	 * @param floorNum
	 * @param updateFile
	 * @param updateMetaData
	 */
	@PutMapping("file/{buildingId}/{floorNum}")
	public ResponseEntity<?> updateFloor(@PathVariable("buildingId")int buildingId,
										@PathVariable("floorNum")int floorNum,
										@RequestPart("updateFile")MultipartFile updateFile,
										@RequestParam("updateMetaData")byte[] updateMetaData) throws IOException {

    byte[] floorData = updateFile.getBytes();

	fileService.floorUpdate(buildingId,floorNum,floorData, updateMetaData);

		return ResponseEntity.ok().body("수정 성공");
	}

	@DeleteMapping("file/{buildingId}")
	public ResponseEntity<?> deleteBuilding(@PathVariable("buildingId")int buildingId) {

		fileService.buildingDelete(buildingId);
		return ResponseEntity.ok().body("삭제 성공");
	}


	/**
	 * 층 삭제
	 * @param buildingId
	 * @param floorNum
	 */
	@DeleteMapping("file/{buildingId}/{floorNum}")
	public ResponseEntity<?> deleteFloor(@PathVariable("buildingId")int buildingId,
										 @PathVariable("floorNum")int floorNum) {

		fileService.floorDelete(buildingId,floorNum);
		return ResponseEntity.ok().body("삭제 성공");
	}






	private static byte[] getDecodeByte(String encodingData) {
		return Base64.getDecoder().decode(encodingData);
	}

	private static int getUserId(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		User user = (User) session.getAttribute(SessionConst.LOGIN_MEMBER);
		return user.getUserId().intValue();
	}

	private static ResponseEntity<BuildingRenderResponse> getBuildingRenderResponse(
		String url, HttpEntity<MultiValueMap<String, Object>> entity) {
		RestTemplate rt = new RestTemplate();
		ResponseEntity<BuildingRenderResponse> response = rt.exchange(
			url,
			HttpMethod.POST,
			entity,
			BuildingRenderResponse.class
		);
		return response;
	}

}
