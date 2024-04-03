package bg.sofia.uni.fmi.mjt.udemy.course.duration;

public record ResourceDuration(int minutes) {
    public ResourceDuration {
        if (minutes > 60 || minutes < 0) {
            throw new IllegalArgumentException("Minutes must be between 0-60!");
        }
    }
}
