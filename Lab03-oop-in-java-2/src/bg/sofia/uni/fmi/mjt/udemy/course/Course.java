package bg.sofia.uni.fmi.mjt.udemy.course;

import bg.sofia.uni.fmi.mjt.udemy.course.duration.CourseDuration;
import bg.sofia.uni.fmi.mjt.udemy.exception.ResourceNotFoundException;

public class Course implements Completable, Purchasable {
    private String name;
    private String description;
    private double price;
    private Resource[] content;
    private Category category;
    private boolean isPurchased;
    private double grade;

    public Course(String name, String description, double price, Resource[] content, Category category) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.content = content;
        this.category = category;
        this.isPurchased = false;
        this.grade = 0;
    }

    @Override
    public boolean isCompleted() {
        for (Resource resource : content) {
            if (!resource.isCompleted()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int getCompletionPercentage() {
        double completion = 0.0;

        int contentLength = content.length;
        for (Resource resource : content) {
            if (resource != null) {
                completion += resource.getCompletionPercentage();
            } else
                contentLength--;
        }

        completion /= contentLength;
        int completionPercentage = (int) Math.round(completion);
        return completionPercentage;
    }

    @Override
    public void purchase() {
        this.isPurchased = true;
    }

    @Override
    public boolean isPurchased() {
        return isPurchased;
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

    public double getPrice() {
        return this.price;
    }

    public Category getCategory() {
        return this.category;
    }

    public Resource[] getContent() {
        return this.content;
    }

    public CourseDuration getTotalTime() {
        return CourseDuration.of(content);
    }

    public void completeResource(Resource resourceToComplete) throws ResourceNotFoundException {
        if (resourceToComplete == null) {
            throw new IllegalArgumentException("Resource is null!");
        }

        for (Resource resource : content) {
            if (resource.equals(resourceToComplete)) {
                resource.complete();
                return;
            }
        }

        throw new ResourceNotFoundException();
    }

    public void addGrade(double grade) {
        this.grade = grade;
    }

    public double getGrade() {
        return this.grade;
    }

    public boolean isLongerThan(Course course) {
        return this.getTotalTime().minutes() > course.getTotalTime().minutes();
    }
}
