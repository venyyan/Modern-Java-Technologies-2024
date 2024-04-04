package bg.sofia.uni.fmi.mjt.itinerary.exception;

public class CityNotKnownException extends Exception {
    public CityNotKnownException(String s) {
        super(s);
    }

    public CityNotKnownException(String s, Throwable cause) {
        super(s, cause);
    }
}
