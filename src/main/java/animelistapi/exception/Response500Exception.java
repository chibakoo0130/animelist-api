package animelistapi.exception;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Response500Exception extends RuntimeException {

    private String errorDetail;

    public Response500Exception(String message, String errorDetail) {
        super(message);
        this.errorDetail = errorDetail;
    }

    public Response500Exception(String message) {
        super(message);
    }
}