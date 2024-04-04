package bg.sofia.uni.fmi.mjt.csvprocessor.exceptions;

public class CsvDataNotCorrectException extends RuntimeException {
    public CsvDataNotCorrectException(String s) {
        super(s);
    }

    public CsvDataNotCorrectException(String s, Throwable cause) {
        super(s, cause);
    }
}
