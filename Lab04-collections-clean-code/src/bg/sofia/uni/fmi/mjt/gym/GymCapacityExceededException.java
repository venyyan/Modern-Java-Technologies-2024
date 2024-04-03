package bg.sofia.uni.fmi.mjt.gym;

public class GymCapacityExceededException extends Exception {
    public GymCapacityExceededException(String s) {
        super(s);
    }

    public GymCapacityExceededException(String s, Throwable cause) {
        super(s, cause);
    }
}
