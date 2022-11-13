package requestcall.dto;

import java.util.Map;

public class ApiResponseDto {
    private Map requestHeader;
    private String responseHeader;

    private Integer statusCode = 200;
    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public String getResponseText() {
        return responseText;
    }

    public void setResponseText(String responseText) {
        this.responseText = responseText;
    }

    private String responseText;

    public Map getResponse() {
        return response;
    }

    public void setResponse(Map response) {
        this.response = response;
    }

    private Map response;
    private String executionTime;

    public Map getErrors() {
        return errors;
    }

    public void setErrors(Map errors) {
        this.errors = errors;
    }

    private Map errors;

    public Map getRequestHeader() {
        return requestHeader;
    }

    public void setRequestHeader(Map requestHeader) {
        this.requestHeader = requestHeader;
    }

    public String getResponseHeader() {
        return responseHeader;
    }

    public void setResponseHeader(String responseHeader) {
        this.responseHeader = responseHeader;
    }

    public String getExecutionTime() {
        return executionTime;
    }

    public void setExecutionTime(String executionTime) {
        this.executionTime = executionTime;
    }
}
