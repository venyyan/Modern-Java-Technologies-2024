package bg.sofia.uni.fmi.mjt.udemy;

import bg.sofia.uni.fmi.mjt.udemy.account.Account;
import bg.sofia.uni.fmi.mjt.udemy.course.Category;
import bg.sofia.uni.fmi.mjt.udemy.course.Course;
import bg.sofia.uni.fmi.mjt.udemy.course.Resource;
import bg.sofia.uni.fmi.mjt.udemy.course.duration.ResourceDuration;
import bg.sofia.uni.fmi.mjt.udemy.exception.AccountNotFoundException;
import bg.sofia.uni.fmi.mjt.udemy.exception.CourseNotFoundException;

public class Udemy implements LearningPlatform {
    private Account[] accounts;
    private Course[] courses;

    public Udemy(Account[] accounts, Course[] courses) {
        this.accounts = accounts;
        this.courses = courses;
    }

    @Override
    public Course findByName(String name) throws CourseNotFoundException {
        if (name.isBlank() || name.isEmpty()) {
            throw new IllegalArgumentException("Name is blank or empty!");
        }
        for (Course course : courses) {
            if (course.getName().equals(name)) {
                return course;
            }
        }

        throw new CourseNotFoundException();
    }

    @Override
    public Course[] findByKeyword(String keyword) {
        if (keyword.isEmpty() || keyword.isBlank() || !Utils.containsOnlyLetters(keyword)) {
            throw new IllegalArgumentException("Keyword is blank or empty, or is not keyword at all!");
        }
        int counterCourses = 0;
        Course[] temp = new Course[courses.length];
        for (Course course : courses) {
            if (course.getName().contains(keyword) || course.getDescription().contains(keyword)) {
                temp[counterCourses++] = course;
            }
        }

        Course[] foundCourses = new Course[counterCourses];
        for (int i = 0; i < counterCourses; i++) {
            foundCourses[i] = temp[i];
        }
        return foundCourses;
    }

    @Override
    public Course[] getAllCoursesByCategory(Category category) {
        if (category == null) {
            throw new IllegalArgumentException("Category is null!");
        }

        Course[] temp = new Course[courses.length];
        int counterCourses = 0;
        for (Course course : courses) {
            if (course.getCategory().equals(category)) {
                temp[counterCourses++] = course;
            }
        }

        Course[] foundCourses = new Course[counterCourses];
        for (int i = 0; i < counterCourses; i++) {
            foundCourses[i] = temp[i];
        }
        return foundCourses;
    }

    @Override
    public Account getAccount(String name) throws AccountNotFoundException {
        if (name.isBlank() || name.isEmpty()) {
            throw new IllegalArgumentException("Name or is blank or empty!");
        }

        for (Account account : accounts) {
            if (account.getUsername().equals(name)) {
                return account;
            }
        }

        throw new AccountNotFoundException();
    }

    @Override
    public Course getLongestCourse() {
        ResourceDuration duration = new ResourceDuration(0);
        Resource resource = new Resource("", duration);
        Resource[] resources = {resource};

        Course longestCourse = new Course("", "", 0.0, resources, null);
        for (Course course : courses) {
            if (course != null && course.isLongerThan(longestCourse)) {
                longestCourse = course;
            }
        }
        return longestCourse;
    }

    @Override
    public Course getCheapestByCategory(Category category) {
        double cheapestPrice = Double.MAX_VALUE;
        Course cheapestCourse = new Course("", "", 0, null, category);
        for (Course course : courses) {
            if (course.getCategory().equals(category) && course.getPrice() < cheapestPrice) {
                cheapestPrice = course.getPrice();
                cheapestCourse = course;
            }
        }
        return cheapestCourse;
    }
}
