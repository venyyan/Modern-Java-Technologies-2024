package bg.sofia.uni.fmi.mjt.udemy.course;

import bg.sofia.uni.fmi.mjt.udemy.course.duration.ResourceDuration;

public class Resource implements Completable {
    private String name;
    private ResourceDuration duration;
    private int completionPercentage;

    private static final int MAX_PERCENTAGE = 100;

    public Resource(String name, ResourceDuration duration) {
        this.name = name;
        this.duration = duration;
        this.completionPercentage = 0;
    }

    @Override
    public boolean isCompleted() {
        return completionPercentage == MAX_PERCENTAGE;
    }

    @Override
    public int getCompletionPercentage() {
        return this.completionPercentage;
    }

    public String getName() {
        return this.name;
    }

    public ResourceDuration getDuration() {
        return this.duration;
    }

    public void complete() {
        this.completionPercentage = MAX_PERCENTAGE;
    }
}
