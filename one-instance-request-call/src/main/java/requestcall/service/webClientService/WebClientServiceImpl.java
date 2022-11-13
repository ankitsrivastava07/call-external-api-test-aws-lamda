package requestcall.service.webClientService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import requestcall.dto.ApiResponseDto;
import requestcall.service.httpService.HttpService;
import requestcall.service.httpService.HttpServiceImpl;

import java.util.Locale;
import java.util.Map;
import java.util.logging.Logger;

public class WebClientServiceImpl implements WebClientService {
    Logger logger = Logger.getLogger("");
    private HttpService httpService;
    public WebClientServiceImpl() {
        this.httpService = new HttpServiceImpl();
    }

    @Override
    public ApiResponseDto callExternalApi(Map requestBody) {
        return callHttpApi(requestBody);
    }

    private ApiResponseDto callHttpApi(Map requestDto) {
        switch (requestDto.get("method").toString().toLowerCase()) {

            case "post": {
                return httpService.post(requestDto);
            }

            case "patch": {
                return httpService.patch(requestDto);
            }

            case "get": {
                return httpService.get(requestDto);
            }

            case "delete": {
                return httpService.delete(requestDto);
            }

            case "put": {
                return httpService.put(requestDto);
            }

            case "options": {
                return httpService.options(requestDto);
            }
        }
        return null;
    }
}
