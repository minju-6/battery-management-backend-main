package wid.bmsbackend.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApiResponse<T> {
    private T body;
    private int status;
    private String error;
    private String errorCode;
    private String message;

    public static <T> ApiResponse<T> success(T body) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setBody(body);
        response.setStatus(HttpStatus.OK.value());
        return response;
    }

    public static ApiResponse<Object> error(String error, String message, String errorCode) {
        ApiResponse<Object> response = new ApiResponse<>();
        response.setError(error);
        if (message != null) {
            response.setMessage(message);
        }
        if (errorCode != null) {
            response.setErrorCode(errorCode);
        }
        return response;
    }

    public static ApiResponse<Object> error(String error, String message) {
        return error(error, message, null);
    }
}
