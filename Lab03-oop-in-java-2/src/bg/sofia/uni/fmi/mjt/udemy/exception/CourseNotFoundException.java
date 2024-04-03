package bg.sofia.uni.fmi.mjt.udemy.exception;

public class CourseNotFoundException extends Exception {
    public CourseNotFoundException() {
        super("Course not found!");
    }

    public CourseNotFoundException(String s) {
        super(s);
    }

    public CourseNotFoundException(String s, Throwable cause) {
        super(s, cause);
    }
}
