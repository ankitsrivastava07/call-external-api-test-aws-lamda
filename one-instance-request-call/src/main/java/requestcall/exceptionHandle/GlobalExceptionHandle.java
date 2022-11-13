package requestcall.exceptionHandle;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import requestcall.dto.ApiResponseDto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandle {
    Logger logger = Logger.getLogger("");
    @Autowired
    private Environment environment;
    Gson gson = new Gson();

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> validationError(MethodArgumentNotValidException exception) {
        Map<String, Object> errors = getAllFieldErrors(exception.getFieldErrors());
        logger.info("Validation Error " + errors);
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    private Map<String, Object> getAllFieldErrors(List<FieldError> errors) {
        Map<String, Object> error = new HashMap<>();
        error.put("serverPort ", environment.getProperty("local.server.port") + " currently running ");
        error.put("errors", errors.stream().collect(Collectors.toMap(e -> e.getField(), e -> e.getDefaultMessage())));
        return error;
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<?> handleJsonParseError(HttpMessageNotReadableException exception) {
        Map<String, Object> errs = new HashMap<>();
        Map<String, String> error = new HashMap<>();
        Throwable throwable = exception.getRootCause();
        if (throwable instanceof InvalidFormatException) {
            error.put("json", " Invalid json request sent");
            errs.put("errors", error);
            logger.info("Invalid Json format given " + exception.getLocalizedMessage());
            return new ResponseEntity<>(errs, HttpStatus.BAD_REQUEST);
        }
        error.put("json", " Invalid json request sent");
        errs.put("errors", error);
        return new ResponseEntity<>(errs, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(TimeoutException.class)
    public ResponseEntity<?> handleTimeoutException(TimeoutException exception) {
        logger.info(" Request timeout occured " + exception.getLocalizedMessage());
        Map error = new HashMap();
        error.put("timeout", "Api taking long time to respond ");
        Map errors = new HashMap();
        errors.put("errors", error);
        return new ResponseEntity<>(errors, HttpStatus.REQUEST_TIMEOUT);
    }

    @ExceptionHandler(WebClientResponseException.BadRequest.class)
    public ResponseEntity<?> handleBadRequest(WebClientResponseException.BadRequest exception) {
        Map error = gson.fromJson(exception.getResponseBodyAsString(), Map.class);
        return new ResponseEntity<>(error, HttpStatus.CREATED);
    }

    @ExceptionHandler(WebClientResponseException.UnsupportedMediaType.class)
    public ResponseEntity<?> handleUnsupportedMediaType(WebClientResponseException.UnsupportedMediaType exception) {
        ApiResponseDto apiResponseDto = new ApiResponseDto();
        apiResponseDto.setResponseText(exception.getResponseBodyAsString());
        apiResponseDto.setResponseHeader(exception.getHeaders().toSingleValueMap().toString());
        return new ResponseEntity<>(apiResponseDto, HttpStatus.CREATED);
    }

    @ExceptionHandler(WebClientResponseException.Unauthorized.class)
    public ResponseEntity<?> handleUnauthorizedEx(WebClientResponseException.Unauthorized exception) {
        ApiResponseDto apiResponseDto = new ApiResponseDto();
        apiResponseDto.setResponseText(exception.getResponseBodyAsString());
        apiResponseDto.setResponseHeader(exception.getHeaders().toSingleValueMap().toString());
        return new ResponseEntity<>(apiResponseDto, HttpStatus.CREATED);
    }

    @ExceptionHandler(WebClientResponseException.GatewayTimeout.class)
    public ResponseEntity<?> badGateway(WebClientResponseException.GatewayTimeout exception) {
        Map error = gson.fromJson(exception.getResponseBodyAsString(), Map.class);
        return new ResponseEntity<>(error, HttpStatus.CREATED);
    }

    @ExceptionHandler(WebClientResponseException.ServiceUnavailable.class)
    public ResponseEntity<?> serviceUnavailable(WebClientResponseException.ServiceUnavailable exception) {
        Map error = gson.fromJson(exception.getResponseBodyAsString(), Map.class);
        return new ResponseEntity<>(error, HttpStatus.CREATED);
    }

    @ExceptionHandler(WebClientResponseException.NotFound.class)
    public ResponseEntity<?> notFound(WebClientResponseException.NotFound exception) {
        ApiResponseDto apiResponseDto = new ApiResponseDto();
        apiResponseDto.setResponseText(exception.getResponseBodyAsString());
        apiResponseDto.setResponseHeader(exception.getHeaders().toSingleValueMap().toString());
        return new ResponseEntity<>(apiResponseDto, HttpStatus.CREATED);
    }

    @ExceptionHandler(WebClientResponseException.MethodNotAllowed.class)
    public ResponseEntity<?> methodNotAllowed(WebClientResponseException.MethodNotAllowed exception) {
        ApiResponseDto apiResponseDto = new ApiResponseDto();
        apiResponseDto.setResponseText(exception.getResponseBodyAsString());
        apiResponseDto.setResponseHeader(exception.getHeaders().toSingleValueMap().toString());
        return new ResponseEntity<>(apiResponseDto, HttpStatus.CREATED);
    }

    @ExceptionHandler(WebClientResponseException.class)
    public ResponseEntity<?> webClientException(WebClientResponseException exception) {
        ApiResponseDto apiResponseDto = new ApiResponseDto();
        apiResponseDto.setResponseText(exception.getResponseBodyAsString());
        apiResponseDto.setResponseHeader(exception.getHeaders().toSingleValueMap().toString());
        return new ResponseEntity<>(apiResponseDto, HttpStatus.CREATED);
    }
}
