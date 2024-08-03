package animelistapi.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Response404Exception.class)
    public ResponseEntity<Error> handle404(Response404Exception ex) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, "application/json; charset=utf-8");

        Error error = new Error();
        error.setStatusCode(404);
        error.setErrorMessage(ex.getMessage());
        return new ResponseEntity<>(error, headers, HttpStatusCode.valueOf(404));
    }

    @ExceptionHandler(Response500Exception.class)
    public ResponseEntity<Error> handle500(Response500Exception ex) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, "application/json; charset=utf-8");

        Error error = new Error();
        error.setStatusCode(500);
        error.setErrorMessage(ex.getMessage());
        return new ResponseEntity<>(error, headers, HttpStatusCode.valueOf(500));
    }
}
