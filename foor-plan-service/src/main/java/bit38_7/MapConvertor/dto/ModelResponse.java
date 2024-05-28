package bit38_7.MapConvertor.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class ModelResponse {

	byte[] floorFileData;
	byte[] metaData;
}
