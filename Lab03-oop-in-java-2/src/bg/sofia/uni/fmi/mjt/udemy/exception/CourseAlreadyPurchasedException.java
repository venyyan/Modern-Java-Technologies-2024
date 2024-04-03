package bg.sofia.uni.fmi.mjt.udemy.exception;

public class CourseAlreadyPurchasedException extends Exception {
    public CourseAlreadyPurchasedException() {
        super("Course already purchased!");
    }

    public CourseAlreadyPurchasedException(String s) {
        super(s);
    }

    public CourseAlreadyPurchasedException(String s, Throwable cause) {
        super(s, cause);
    }
}
