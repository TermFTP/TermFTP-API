package at.termftp.backend.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Error {
    private int statusCode;
    private String title;
    private String errorMessage;

    public Error() {
    }

    public Error(@JsonProperty("statusCode") int statusCode,
                 @JsonProperty("title") String title,
                 @JsonProperty("errorMessage") String errorMessage) {
        this.statusCode = statusCode;
        this.title = title;
        this.errorMessage = errorMessage;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Error error = (Error) o;

        if (statusCode != error.statusCode) return false;
        if (title != null ? !title.equals(error.title) : error.title != null) return false;
        return errorMessage != null ? errorMessage.equals(error.errorMessage) : error.errorMessage == null;
    }

    @Override
    public int hashCode() {
        int result = statusCode;
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (errorMessage != null ? errorMessage.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Error{" +
                "statusCode=" + statusCode +
                ", title='" + title + '\'' +
                ", errorMessage='" + errorMessage + '\'' +
                '}';
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
