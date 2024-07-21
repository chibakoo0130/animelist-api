package animelistapi.exception;

import lombok.Data;

@Data
public class Error {

    private int statusCode;
    private String errorMessage;
    private String errorDetail;
}
