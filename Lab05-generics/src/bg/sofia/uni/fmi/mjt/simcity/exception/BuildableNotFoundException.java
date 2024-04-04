package bg.sofia.uni.fmi.mjt.simcity.exception;

public class BuildableNotFoundException extends RuntimeException {
    public BuildableNotFoundException(String s) {
        super(s);
    }

    public BuildableNotFoundException(String s, Throwable cause) {
        super(s, cause);
    }
}
