package at.termftp.backend.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Error {
    private int status;
    private String title;
    private String message;

    public Error() {
    }

    public Error(@JsonProperty("status") int status,
                 @JsonProperty("title") String title,
                 @JsonProperty("message") String message) {
        this.status = status;
        this.title = title;
        this.message = message;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Error error = (Error) o;

        if (status != error.status) return false;
        if (title != null ? !title.equals(error.title) : error.title != null) return false;
        return message != null ? message.equals(error.message) : error.message == null;
    }

    @Override
    public int hashCode() {
        int result = status;
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (message != null ? message.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Error{" +
                "statusCode=" + status +
                ", title='" + title + '\'' +
                ", errorMessage='" + message + '\'' +
                '}';
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
