package at.termftp.backend.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class DefaultResponse {
    private int status;
    private String message;
    private Object data;

    public static ResponseEntity<DefaultResponse> createResponse(Object data, String message){

        if(data instanceof Error){
            Error error = (Error) data;
            return ResponseEntity.status(error.getStatus()).body(new DefaultResponse(error.getStatus(), error.getTitle(), error.getMessage()));
        }
        int status = HttpStatus.OK.value();
        return ResponseEntity.status(status).body(new DefaultResponse(status, message == null ? "OK" : message, data));
    }

    public DefaultResponse(@JsonProperty("status") int status,
                           @JsonProperty("message") String message,
                           @JsonProperty("data") Object data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public DefaultResponse() {
    }

    @Override
    public String toString() {
        return "DefaultResponse{" +
                "status=" + status +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DefaultResponse that = (DefaultResponse) o;

        if (status != that.status) return false;
        if (message != null ? !message.equals(that.message) : that.message != null) return false;
        return data != null ? data.equals(that.data) : that.data == null;
    }

    @Override
    public int hashCode() {
        int result = status;
        result = 31 * result + (message != null ? message.hashCode() : 0);
        result = 31 * result + (data != null ? data.hashCode() : 0);
        return result;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
