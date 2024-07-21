package animelistapi.exception;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Response404Exception extends RuntimeException {

    private String errorDetail;

    public Response404Exception(String message, String errorDetail) {
        super(message);
        this.errorDetail = errorDetail;
    }

    public Response404Exception(String message) {
        super(message);
    }
}