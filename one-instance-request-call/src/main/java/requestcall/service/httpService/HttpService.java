package requestcall.service.httpService;
import requestcall.dto.ApiResponseDto;
import java.util.Map;
public interface HttpService {

    ApiResponseDto post(Map request);
    ApiResponseDto patch(Map request);

    ApiResponseDto delete(Map request);

    ApiResponseDto get(Map request);

    ApiResponseDto put(Map request);

    ApiResponseDto options(Map request);
}
