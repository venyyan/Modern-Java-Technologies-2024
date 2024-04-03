package bg.sofia.uni.fmi.mjt.udemy.exception;

public class ResourceNotFoundException extends Exception {
    public ResourceNotFoundException() {
        super("Can't find resource!");
    }

    public ResourceNotFoundException(String s) {
        super(s);
    }

    public ResourceNotFoundException(String s, Throwable cause) {
        super(s, cause);
    }
}
