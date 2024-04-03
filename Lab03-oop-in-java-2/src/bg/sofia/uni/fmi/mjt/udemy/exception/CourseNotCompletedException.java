package bg.sofia.uni.fmi.mjt.udemy.exception;

public class CourseNotCompletedException extends Exception {
    public CourseNotCompletedException() {
        super("Course not completed!");
    }

    public CourseNotCompletedException(String s) {
        super(s);
    }

    public CourseNotCompletedException(String s, Throwable cause) {
        super(s, cause);
    }
}
