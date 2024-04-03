package bg.sofia.uni.fmi.mjt.udemy.exception;

public class CourseNotPurchasedException extends Exception {
    public CourseNotPurchasedException() {
        super("Course is not purchased yet!");
    }

    public CourseNotPurchasedException(String s) {
        super(s);
    }

    public CourseNotPurchasedException(String s, Throwable cause) {
        super(s, cause);
    }
}
