package bg.sofia.uni.fmi.mjt.simcity.exception;

public class InsufficientPlotAreaException extends RuntimeException {
    public InsufficientPlotAreaException(String s) {
        super(s);
    }

    public InsufficientPlotAreaException(String s, Throwable cause) {
        super(s, cause);
    }
}
