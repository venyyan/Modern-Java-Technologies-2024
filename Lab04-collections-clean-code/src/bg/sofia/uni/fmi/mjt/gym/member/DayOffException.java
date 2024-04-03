package bg.sofia.uni.fmi.mjt.gym.member;

public class DayOffException extends RuntimeException {
    public DayOffException(String s) {
        super(s);
    }

    public DayOffException(String s, Throwable cause) {
        super(s, cause);
    }
}
