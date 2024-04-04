package bg.sofia.uni.fmi.mjt.itinerary.exception;

public class NoPathToDestinationException extends Exception {
    public NoPathToDestinationException(String s) {
        super(s);
    }

    public NoPathToDestinationException(String s, Throwable cause) {
        super(s, cause);
    }
}
