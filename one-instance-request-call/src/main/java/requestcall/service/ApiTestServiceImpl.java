package requestcall.service;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.util.StopWatch;
import org.springframework.util.StringUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import requestcall.constant.RequestConstant;
import requestcall.dto.ApiResponseDto;
import requestcall.dto.RequestDto;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
public class ApiTestServiceImpl implements ApiTestService {
    public static final String HTTP_REQUEST = "HTTP_REQUEST";

    @Override
    public ApiResponseDto callExternalApi(RequestDto requestDto) {
        Map response = jsonValidation(requestDto);

        if (!(boolean) response.get("flag")) {
            ApiResponseDto apiResponseDto = new ApiResponseDto();
            apiResponseDto.setStatusCode((Integer) response.get(HTTP_REQUEST));
            response.remove(HTTP_REQUEST);
            response.remove("flag");
            apiResponseDto.setErrors(response);
            return apiResponseDto;
        }
        return callHttpApi(response);
    }

    public Map jsonValidation(RequestDto requestDto) {
        Map error;
        if (!(boolean) (error = requestBodyValidation(requestDto)).get("flag")) {
            return error;
        }
        error.put("flag", Boolean.TRUE);
        return error;
    }

    private Map requestBodyValidation(RequestDto requestDto) {
        String uri = "";
        String method = "";
        Map<String, String> httpMethod = new HashMap<>();
        httpMethod.put("get", "get");
        httpMethod.put("post", "post");
        httpMethod.put("put", "put");
        httpMethod.put("delete", "delete");
        httpMethod.put("patch", "patch");
        httpMethod.put("head", "head");
        httpMethod.put("options", "options");

        Map errors = new HashMap();
        Map error = new HashMap();
        errors.put(HTTP_REQUEST, 200);
        errors.put("flag", Boolean.TRUE);
        try {

            if (!StringUtils.hasLength(method = requestDto.getMethodType())) {
                error.put("methodType", "Method can't be null or empty");
                errors.put("flag", Boolean.FALSE);
                errors.put(HTTP_REQUEST, 400);
            } else if (StringUtils.hasLength(method) && httpMethod.get(method = method.toLowerCase()) == null) {
                error.put("method", "Invalid Http method, Allowed Methods are Post , Put , Get , Patch , Delete , Options ");
                errors.put("errors", error);
                errors.put("flag", Boolean.FALSE);
                errors.put(HTTP_REQUEST, 400);
            }

            if (!StringUtils.hasLength(uri = requestDto.getUri())) {
                error.put("uri", "Api can't be null or empty");
                errors.put("flag", Boolean.FALSE);
                errors.put(HTTP_REQUEST, 400);
            } else if (StringUtils.hasLength(uri)) {
                try {
                    URL url = new URL(uri);
                    url.openConnection().connect();
                } catch (IOException exception) {
                    if (exception instanceof MalformedURLException) {
                        error.put("api", "Invalid Api ");
                        errors.put("flag", Boolean.FALSE);
                        errors.put(HTTP_REQUEST, 400);
                    } else if (exception instanceof IOException) {
                        error.put("message", "Unable to connect with the given " + uri);
                        errors.put("errors", error);
                        errors.put(RequestConstant.HTTP_REQUEST, 400);
                        errors.put("flag", false);
                    }
                }
            }
         // Validate Json Request Body in Post Api Call
            Map data = new Gson().fromJson(requestDto.getRequestBody(), Map.class);
            if ((boolean) errors.get("flag")) {
                Map reqBody = new HashMap();
                reqBody.put("uri", requestDto.getUri());
                reqBody.put("method", requestDto.getMethodType().toLowerCase());
                reqBody.put("requestBody", (requestDto.getRequestBody() == null || requestDto.getRequestBody().isEmpty())
                        ? new HashMap<>() : data);
                reqBody.put("flag",Boolean.TRUE);
                return reqBody;
            }
            errors.put("errors", error);
            return errors;
        } catch (JsonSyntaxException exception) {
            error.put("requestBody", "Api Call not performed due to invalid json, please provide valid json format ");
            errors.put(HTTP_REQUEST, 400);

            errors.put("flag", Boolean.FALSE);
            errors.put("errors", error);
            // logger.info("Invalid json, please provide valid json format  " + errors);
            return errors;
        }
    }

    private ApiResponseDto callHttpApi(Map requestDto) {
        switch ((String) requestDto.get("method")) {

            case "post": {
                return post(requestDto);
            }

            case "patch": {
                return patch(requestDto);
            }

            case "get": {
                return get(requestDto);
            }

            case "delete": {
                return delete(requestDto);
            }

            case "put": {
                return put(requestDto);
            }

            case "options": {
                return options(requestDto);
            }

            case "head": {
                return head(requestDto);
            }
        }
        ApiResponseDto responseDto = new ApiResponseDto();
        responseDto.setResponseText(requestDto.get("method").toString());
        return responseDto;
    }
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
            ResponseEntity<String> response = getRestTemplate().exchange(requestVal.get("uri").toString(),
                    HttpMethod.POST, getHttpEntity(requestVal.get("requestBody")), String.class);
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
            ResponseEntity<String> responseEntity = getRestTemplate().exchange((String) request.get("uri"),
                    HttpMethod.PATCH, getHttpEntity(request), String.class);
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
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
        requestFactory.setConnectTimeout(5000);
        requestFactory.setReadTimeout(1000);
        return new RestTemplate(requestFactory);
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
        if (exception.getCause() instanceof SocketTimeoutException) {
            Map errors = new HashMap();
            Map error = new HashMap();

            error.put("message", "Your api taking long time to respond");
            errors.put("errors", error);
            responseDto.setResponseText(errors.toString());
            responseDto.setStatusCode(400);
            return responseDto;
        }
        responseDto.setResponseText("Error occurred" + exception.getMessage());
        return responseDto;
    }
}
