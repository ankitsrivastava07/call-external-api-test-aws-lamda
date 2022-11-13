package requestcall.requestHanldler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import requestcall.dto.ApiResponseDto;
import requestcall.dto.RequestDto;
import requestcall.service.ApiTestService;
import requestcall.service.ApiTestServiceImpl;

public class AwsRequestHandler implements RequestHandler<RequestDto, ApiResponseDto> {

    @Override
    public ApiResponseDto handleRequest(RequestDto input, Context context) {
        ApiTestService apiTestService = new ApiTestServiceImpl();
        ApiResponseDto apiResponseDto = apiTestService.callExternalApi(input);
        return apiResponseDto;
    }
}
