package requestcall.service.webClientService;

import requestcall.dto.ApiResponseDto;
import java.util.Map;

public interface WebClientService {
    ApiResponseDto callExternalApi(Map requestBody);
}
