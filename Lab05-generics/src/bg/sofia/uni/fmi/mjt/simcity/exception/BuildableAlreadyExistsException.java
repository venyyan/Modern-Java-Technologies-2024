package bg.sofia.uni.fmi.mjt.simcity.exception;

public class BuildableAlreadyExistsException extends RuntimeException {
    public BuildableAlreadyExistsException(String s) {
        super(s);
    }

    public BuildableAlreadyExistsException(String s, Throwable cause) {
        super(s, cause);
    }
}
