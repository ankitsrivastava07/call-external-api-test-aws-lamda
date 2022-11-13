package requestcall.service;
import requestcall.dto.ApiResponseDto;
import requestcall.dto.RequestDto;
import java.util.Map;
public interface ApiTestService {
    ApiResponseDto callExternalApi(RequestDto requestDto);
}
