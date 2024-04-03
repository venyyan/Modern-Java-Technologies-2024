package bg.sofia.uni.fmi.mjt.udemy.account;

import bg.sofia.uni.fmi.mjt.udemy.account.type.AccountType;
import bg.sofia.uni.fmi.mjt.udemy.course.Course;
import bg.sofia.uni.fmi.mjt.udemy.exception.CourseAlreadyPurchasedException;
import bg.sofia.uni.fmi.mjt.udemy.exception.InsufficientBalanceException;
import bg.sofia.uni.fmi.mjt.udemy.exception.MaxCourseCapacityReachedException;

public class StandardAccount extends AccountBase {
    public StandardAccount(String username, double balance) {
        super(username, balance);
        this.accountType = AccountType.STANDARD;
    }

    @Override
    public void buyCourse(Course course) throws InsufficientBalanceException, CourseAlreadyPurchasedException, MaxCourseCapacityReachedException {
        if (purchasedCoursesCount >= MAX_COURSES_CAPACITY) {
            throw new MaxCourseCapacityReachedException();
        }
        if (isCoursePurchased(course)) {
            throw new CourseAlreadyPurchasedException();
        }
        if (course.getPrice() > balance) {
            throw new InsufficientBalanceException();
        }
        this.balance -= course.getPrice();
        this.purchasedCourses[purchasedCoursesCount++] = course;
    }
}