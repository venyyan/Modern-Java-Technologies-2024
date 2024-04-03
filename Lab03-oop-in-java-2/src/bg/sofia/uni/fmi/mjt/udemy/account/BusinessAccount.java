package bg.sofia.uni.fmi.mjt.udemy.account;

import bg.sofia.uni.fmi.mjt.udemy.account.type.AccountType;
import bg.sofia.uni.fmi.mjt.udemy.course.Category;
import bg.sofia.uni.fmi.mjt.udemy.course.Course;
import bg.sofia.uni.fmi.mjt.udemy.exception.CourseAlreadyPurchasedException;
import bg.sofia.uni.fmi.mjt.udemy.exception.InsufficientBalanceException;
import bg.sofia.uni.fmi.mjt.udemy.exception.MaxCourseCapacityReachedException;


public class BusinessAccount extends AccountBase {
    private Category[] allowedCategories;

    public BusinessAccount(String username, double balance, Category[] allowedCategories) {
        super(username, balance);
        this.allowedCategories = allowedCategories;
        this.accountType = AccountType.BUSINESS;
    }

    @Override
    public void buyCourse(Course course) throws InsufficientBalanceException, CourseAlreadyPurchasedException, MaxCourseCapacityReachedException {
        if (purchasedCoursesCount >= MAX_COURSES_CAPACITY) {
            throw new MaxCourseCapacityReachedException();
        }

        for (Category category : allowedCategories) {
            if (course.getCategory().equals(category)) {
                if (isCoursePurchased(course)) {
                    throw new CourseAlreadyPurchasedException();
                }
                if (this.balance >= course.getPrice()) {
                    this.balance -= (course.getPrice() - course.getPrice() * accountType.getDiscount());
                    this.purchasedCourses[purchasedCoursesCount++] = course;
                    return;
                } else {
                    throw new InsufficientBalanceException();
                }
            }
        }
        throw new IllegalArgumentException("Course is not in the allowed categories for this Business account!");
    }
}
