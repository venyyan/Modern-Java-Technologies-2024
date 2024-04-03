package bg.sofia.uni.fmi.mjt.udemy.course.duration;

import bg.sofia.uni.fmi.mjt.udemy.course.Resource;

public record CourseDuration(int hours, int minutes) {
    public CourseDuration {
        if (hours >= 24 || hours < 0) {
            throw new IllegalArgumentException("Hours must be between 0-24");
        }

        if (minutes > 60 || minutes < 0) {
            throw new IllegalArgumentException("Minutes must be between 0-60!");
        }
    }

    public static CourseDuration of(Resource[] content) {
        int totalMinutes = 0;
        for (Resource resource : content) {
            totalMinutes += resource.getDuration().minutes();
        }
        int courseHours = totalMinutes / 60;
        int courseMinutes = totalMinutes - courseHours;
        return new CourseDuration(courseHours, courseMinutes);
    }
}
