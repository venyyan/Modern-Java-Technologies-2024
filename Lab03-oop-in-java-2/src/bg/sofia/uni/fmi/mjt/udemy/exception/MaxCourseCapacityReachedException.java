package bg.sofia.uni.fmi.mjt.udemy.exception;

public class MaxCourseCapacityReachedException extends Exception {
    public MaxCourseCapacityReachedException() {
        super("Cannot buy more courses! Max capacity is 100.");
    }

    public MaxCourseCapacityReachedException(String s) {
        super(s);
    }

    public MaxCourseCapacityReachedException(String s, Throwable cause) {
        super(s, cause);
    }
}
