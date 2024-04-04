package bg.sofia.uni.fmi.mjt.food.exception;

public class ErrorResponseException extends Exception {

    public ErrorResponseException(String message) {
        super(message);
    }

    public ErrorResponseException(String message, Throwable cause) {
        super(message, cause);
    }

    public ErrorResponseException(int statusCode, String message) {
        super("Error while trying to receive recipes. Status code: " + statusCode + "; Message: "
            + message);
    }
}
