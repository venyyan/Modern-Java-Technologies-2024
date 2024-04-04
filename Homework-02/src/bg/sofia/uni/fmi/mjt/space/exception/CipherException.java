package bg.sofia.uni.fmi.mjt.space.exception;

public class CipherException extends Exception {
    public CipherException(String s) {
        super(s);
    }

    public CipherException(String s, Throwable cause) {
        super(s, cause);
    }
}
