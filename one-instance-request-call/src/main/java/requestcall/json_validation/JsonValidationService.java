package requestcall.json_validation;

import requestcall.dto.RequestDto;
import java.util.Map;

public interface JsonValidationService {
    Map jsonValidation(RequestDto requestDto);
}
