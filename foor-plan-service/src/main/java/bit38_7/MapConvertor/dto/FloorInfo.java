package bit38_7.MapConvertor.dto;


import java.time.LocalDate;
import java.util.Date;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class FloorInfo {

	int floorNum;
	LocalDate updateDate;
	boolean isNull;
}
