package requestcall.dto;

public class RequestDto {
    private String requestBody;
    private String uri;
    public String getRequestBody() {
        return requestBody;
    }

    public void setRequestBody(String requestJson) {
        this.requestBody = requestJson;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getMethodType() {
        return methodType;
    }

    public void setMethodType(String methodType) {
        this.methodType = methodType;
    }

    private String methodType;

    @Override
    public String toString() {
        return "RequestDto{" +
                "requestBody=" +
                ", uri='" + uri + '\'' +
                ", methodType='" + methodType + '\'' +
                '}';
    }
}
