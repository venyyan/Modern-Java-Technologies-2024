package bg.sofia.uni.fmi.mjt.udemy.account;

import bg.sofia.uni.fmi.mjt.udemy.account.type.AccountType;
import bg.sofia.uni.fmi.mjt.udemy.course.Course;
import bg.sofia.uni.fmi.mjt.udemy.course.Resource;
import bg.sofia.uni.fmi.mjt.udemy.exception.CourseNotCompletedException;
import bg.sofia.uni.fmi.mjt.udemy.exception.CourseNotPurchasedException;
import bg.sofia.uni.fmi.mjt.udemy.exception.ResourceNotFoundException;

public abstract class AccountBase implements Account {
    protected String username;
    protected double balance;
    protected AccountType accountType;
    protected Course[] purchasedCourses;
    protected int purchasedCoursesCount;

    protected static final int MAX_COURSES_CAPACITY = 100;

    public AccountBase(String username, double balance) {
        this.username = username;
        this.balance = balance;
        purchasedCourses = new Course[MAX_COURSES_CAPACITY];
        this.purchasedCoursesCount = 0;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public void addToBalance(double amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Amount should be more than 0!");
        }

        this.balance += amount;
    }

    @Override
    public double getBalance() {
        return this.balance;
    }

    public boolean isCoursePurchased(Course course) {
        for (int i = 0; i < purchasedCoursesCount; i++) {
            if (course.equals(purchasedCourses[i])) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void completeResourcesFromCourse(Course course, Resource[] resourcesToComplete) throws CourseNotPurchasedException, ResourceNotFoundException {
        if (!isCoursePurchased(course)) {
            throw new CourseNotPurchasedException();
        }
        if (course == null || resourcesToComplete == null) {
            throw new IllegalArgumentException("Course or resources is null!");
        }

        Resource[] courseContent = course.getContent();
        for (Resource resourceToComplete : resourcesToComplete) {
            boolean foundResource = false;
            for (Resource courseResource : courseContent) {
                if (resourceToComplete.equals(courseResource)) {
                    course.completeResource(resourceToComplete);
                    foundResource = true;
                    break;
                }
            }
            if (!foundResource)
                throw new ResourceNotFoundException();
        }
    }

    @Override
    public void completeCourse(Course course, double grade) throws CourseNotPurchasedException, CourseNotCompletedException {
        if (grade > 6 || grade < 2) {
            throw new IllegalArgumentException("Grade should be between 2-6!");
        }
        if (!isCoursePurchased(course)) {
            throw new CourseNotPurchasedException();
        }

        Resource[] courseContent = course.getContent();
        for (Resource resource : courseContent) {
            if (!resource.isCompleted()) {
                throw new CourseNotCompletedException();
            }
        }
        course.addGrade(grade);
    }

    @Override
    public Course getLeastCompletedCourse() {
        if (purchasedCoursesCount == 0) {
            return null;
        }

        int leastCompletionPercentage = 110;
        Course leastCompletedCourse = new Course("", "", 0.0, null, null);
        for (int i = 0; i < purchasedCoursesCount; i++) {
            if (purchasedCourses[i].getCompletionPercentage() < leastCompletionPercentage) {
                leastCompletionPercentage = purchasedCourses[i].getCompletionPercentage();
                leastCompletedCourse = purchasedCourses[i];
            }
        }

        return leastCompletedCourse;
    }
}
