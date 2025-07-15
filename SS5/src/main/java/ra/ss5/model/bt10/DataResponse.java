package ra.ss5.model.bt10;

import org.springframework.http.HttpStatus;

public class DataResponse<T> {
    private T data;
    private HttpStatus status;
    private String message;
    private int statusCode;

    public DataResponse() {}

    public DataResponse(T data, HttpStatus status) {
        this.data = data;
        this.status = status;
        this.statusCode = status.value();
        this.message = status.getReasonPhrase();
    }

    public DataResponse(T data, HttpStatus status, String message) {
        this.data = data;
        this.status = status;
        this.statusCode = status.value();
        this.message = message;
    }

    // Getters and Setters
    public T getData() { return data; }
    public void setData(T data) { this.data = data; }

    public HttpStatus getStatus() { return status; }
    public void setStatus(HttpStatus status) { this.status = status; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public int getStatusCode() { return statusCode; }
    public void setStatusCode(int statusCode) { this.statusCode = statusCode; }
}
