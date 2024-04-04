package bg.sofia.uni.fmi.mjt.space.exception;

public class TimeFrameMismatchException extends RuntimeException {
    public TimeFrameMismatchException(String s) {
        super(s);
    }

    public TimeFrameMismatchException(String s, Throwable cause) {
        super(s, cause);
    }
}
