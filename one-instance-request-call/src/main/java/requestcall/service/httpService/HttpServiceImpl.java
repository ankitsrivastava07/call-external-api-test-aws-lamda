package requestcall.service.httpService;

import com.google.gson.Gson;
import io.netty.handler.timeout.TimeoutException;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import requestcall.dto.ApiResponseDto;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

@Service
public class HttpServiceImpl implements HttpService {
    Logger logger = Logger.getLogger("");

    public ApiResponseDto get(Map request) {
        ResponseEntity<String> responseEntity;
        StopWatch stopWatch = new StopWatch();
        try {
            stopWatch.start();
            responseEntity = getRestTemplate().exchange((String) request.get("uri"), HttpMethod.GET, getHttpEntity(request), String.class);
            return setResponseAndHeader(responseEntity, stopWatch);
        } catch (HttpClientErrorException exception) {
            return setResponseAndHeader(exception, stopWatch);
        } catch (Exception exception) {
            return handleUnknowException(exception);
        }
    }

    public ApiResponseDto post(Map requestVal) {
        StopWatch stopwatch = new StopWatch();
        try {
            stopwatch.start();
            ResponseEntity<String> response = getRestTemplate().exchange((String) requestVal.get("uri"),
                    HttpMethod.POST, getHttpEntity(requestVal), String.class);
            return setResponseAndHeader(response, stopwatch);
        } catch (HttpClientErrorException exception) {
            return setResponseAndHeader(exception, stopwatch);
        } catch (Exception exception) {
            return handleUnknowException(exception);
        }
    }

    public ApiResponseDto patch(Map request) {
        StopWatch stopWatch = new StopWatch();
        try {
            stopWatch.start();
            ResponseEntity<String> responseEntity = getRestTemplate().exchange((String) request.get("uri"), HttpMethod.PATCH, getHttpEntity(request), String.class);
            return setResponseAndHeader(responseEntity, stopWatch);
        } catch (HttpClientErrorException exception) {
            return setResponseAndHeader(exception, stopWatch);
        } catch (Exception exception) {
            return handleUnknowException(exception);
        }
    }

    public ApiResponseDto put(Map request) {
        StopWatch stopWatch = new StopWatch();
        try {
            stopWatch.start();
            ResponseEntity<String> responseEntity = getRestTemplate().exchange((String) request.get("uri"), HttpMethod.PUT, getHttpEntity(request), String.class);
            return setResponseAndHeader(responseEntity, stopWatch);
        } catch (HttpClientErrorException exception) {
            return setResponseAndHeader(exception, stopWatch);
        } catch (Exception exception) {
            return handleUnknowException(exception);
        }
    }

    public ApiResponseDto delete(Map request) {
        StopWatch stopWatch = new StopWatch();
        try {
            stopWatch.start();
            ResponseEntity<String> responseEntity = getRestTemplate().exchange((String) request.get("uri"), HttpMethod.DELETE, getHttpEntity(request), String.class);
            return setResponseAndHeader(responseEntity, stopWatch);
        } catch (HttpClientErrorException exception) {
            return setResponseAndHeader(exception, stopWatch);
        } catch (Exception exception) {
            return handleUnknowException(exception);
        }
    }

    public ApiResponseDto options(Map request) {
        StopWatch stopWatch = new StopWatch();
        try {
            stopWatch.start();
            ResponseEntity<String> responseEntity = getRestTemplate().exchange((String) request.get("uri"), HttpMethod.OPTIONS, getHttpEntity(request), String.class);
            return setResponseAndHeader(responseEntity, stopWatch);
        } catch (HttpClientErrorException exception) {
            return setResponseAndHeader(exception, stopWatch);
        } catch (Exception exception) {
            return handleUnknowException(exception);
        }
    }
    public ApiResponseDto head(Map request) {
        StopWatch stopWatch = new StopWatch();
        try {
            stopWatch.start();
            ResponseEntity<String> responseEntity = getRestTemplate().exchange((String) request.get("uri"), HttpMethod.HEAD, getHttpEntity(request), String.class);
            return setResponseAndHeader(responseEntity, stopWatch);
        } catch (HttpClientErrorException exception) {
            return setResponseAndHeader(exception, stopWatch);
        } catch (Exception exception) {
            return handleUnknowException(exception);
        }
    }

    private String getUriFromMap(Map requestDto) {
        return requestDto.get("uri").toString();
    }

    private <T> HttpEntity<T> getHttpEntity(T entity) {
        return new HttpEntity<>(entity);
    }

    private RestTemplate getRestTemplate() {
        return new RestTemplateBuilder()
                .setConnectTimeout(Duration.ofMillis(2000))
                .setReadTimeout(Duration.ofMillis(8000))
                .build();
    }

    private ApiResponseDto setResponseAndHeader(Object elements, StopWatch stopWatch) {
        ApiResponseDto apiResponseDto = new ApiResponseDto();
        stopWatch.stop();
        if (elements instanceof ResponseEntity) {
            ResponseEntity<String> responseEntity = (ResponseEntity<String>) elements;
            apiResponseDto.setResponseHeader(responseEntity.getHeaders().toSingleValueMap().toString());
            apiResponseDto.setResponseText(responseEntity.getBody());
            apiResponseDto.setExecutionTime(stopWatch.getLastTaskTimeMillis() + "ms");
            return apiResponseDto;
        } else {
            HttpClientErrorException httpClientErrorException = (HttpClientErrorException) elements;
            apiResponseDto.setResponseText(httpClientErrorException.getResponseBodyAsString());
            apiResponseDto.setResponseHeader(httpClientErrorException.getResponseHeaders().toSingleValueMap().toString());
            apiResponseDto.setExecutionTime(stopWatch.getLastTaskTimeMillis() + "ms");
            return apiResponseDto;
        }
    }

    private ApiResponseDto handleUnknowException(Exception exception) {
        ApiResponseDto responseDto = new ApiResponseDto();
        if (exception instanceof ResourceAccessException) {
            Map errors = new HashMap();
            Map error = new HashMap();
            error.put("message", "Your api taking long time to respond");
            errors.put("errors", error);
            responseDto.setResponseText(errors.toString());
            responseDto.setStatusCode(400);
            return responseDto;
        }
        responseDto.setResponseText("Error occurred");
        return responseDto;
    }
}
