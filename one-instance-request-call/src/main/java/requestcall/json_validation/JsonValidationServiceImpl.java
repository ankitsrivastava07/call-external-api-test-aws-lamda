package requestcall.json_validation;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import org.springframework.util.StringUtils;
import requestcall.constant.RequestConstant;
import requestcall.dto.RequestDto;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import static requestcall.constant.RequestConstant.HTTP_REQUEST;

public class JsonValidationServiceImpl implements JsonValidationService {
    Logger logger = Logger.getLogger("");

    @Override
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
        Map errors = new HashMap();
        Map error = new HashMap();
        errors.put(HTTP_REQUEST, 200);
        errors.put("flag", Boolean.TRUE);
        try {

            if (!StringUtils.hasLength(method = requestDto.getMethodType())) {
                error.put("methodType", "Method can't be null or empty");
                errors.put("flag", Boolean.FALSE);
                errors.put(HTTP_REQUEST, 400);
            }

            if (!StringUtils.hasLength(uri = requestDto.getUri())) {
                error.put("uri", "Api can't be null or empty");
                errors.put("flag", Boolean.FALSE);
                errors.put(HTTP_REQUEST, 400);
            }
            else if (StringUtils.hasLength(uri)) {
                try {
                    new URL(uri).openConnection().connect();
                } catch (IOException exception) {
                    error.put("message", "Unable to connect with the uri " + uri);
                    errors.put("errors", error);
                    errors.put(RequestConstant.HTTP_REQUEST, 400);
                    errors.put("flag", false);
                    return errors;
                }
            }
            Map data = new Gson().fromJson(requestDto.getRequestBody(), Map.class);
            if ((boolean) errors.get("flag")) {
                errors.put("uri", uri);
                errors.put("method", method);
                errors.put("requestBody", (data == null || data.isEmpty()) ? new HashMap<>() : data);
                return errors;
            }
            errors.put("errors", error);
            return errors;
        } catch (JsonSyntaxException exception) {
            error.put("requestBody", "Api Call not performed due to invalid json, please provide valid json format ");
            errors.put(HTTP_REQUEST, 400);

            errors.put("flag", Boolean.FALSE);
            errors.put("errors", error);
            logger.info("Invalid json, please provide valid json format  " + errors);
            return errors;
        }
    }
}
