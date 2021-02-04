package at.termftp.backend.api;

import at.termftp.backend.model.DefaultResponse;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * handles exceptions that are caused by parsing errors (json-deserialization)
     * @return ResponseEntity with DefaultResponse
     */
    @Override
    @ExceptionHandler(value = {InvalidFormatException.class})
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers,
                                                                  HttpStatus status, WebRequest request) {
        return ResponseEntity.status(status.value()).body(new DefaultResponse(status.value(), ex.getMessage(), null));
    }


    /**
     * handles all other exceptions
     * @return ResponseEntity with DefaultResponse
     */
    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String message = null;

        // exceptions that are caused by missing (wrong) request headers
        if(ex instanceof MissingRequestHeaderException){
            message = "Please include the missing request header: " + ((MissingRequestHeaderException)ex).getHeaderName();
            return ResponseEntity.status(status.value()).body(new DefaultResponse(status.value(), "MissingRequestHeaderException",
                    message));
        }

        // default exception
        return ResponseEntity.status(status.value()).body(new DefaultResponse(status.value(), ex.getMessage(), message));
    }
}
