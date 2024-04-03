package bg.sofia.uni.fmi.mjt.udemy.account;

import bg.sofia.uni.fmi.mjt.udemy.account.type.AccountType;
import bg.sofia.uni.fmi.mjt.udemy.course.Course;
import bg.sofia.uni.fmi.mjt.udemy.exception.CourseAlreadyPurchasedException;
import bg.sofia.uni.fmi.mjt.udemy.exception.InsufficientBalanceException;
import bg.sofia.uni.fmi.mjt.udemy.exception.MaxCourseCapacityReachedException;

public class EducationalAccount extends AccountBase {
    private int usedDiscounts;
    private static final double MINIMUM_GRADE_FOR_DISCOUNT = 4.50;
    private static final int CONSECUTIVE_COURSES_FOR_DISCOUNT = 5;

    public EducationalAccount(String username, double balance) {
        super(username, balance);
        this.accountType = AccountType.EDUCATION;
        this.usedDiscounts = 0;
    }

    @Override
    public void buyCourse(Course course) throws InsufficientBalanceException, CourseAlreadyPurchasedException, MaxCourseCapacityReachedException {
        if (purchasedCoursesCount >= MAX_COURSES_CAPACITY) {
            throw new MaxCourseCapacityReachedException();
        }
        if (isCoursePurchased(course)) {
            throw new CourseAlreadyPurchasedException();
        }
        if (canGetDiscount()) {
            if (course.getPrice() * this.accountType.getDiscount() > balance) {
                throw new InsufficientBalanceException();
            }
            this.balance -= (course.getPrice() - course.getPrice() * this.accountType.getDiscount());
            this.usedDiscounts++;
        } else {
            if (course.getPrice() > balance) {
                throw new InsufficientBalanceException();
            }
            this.balance -= course.getPrice();
        }
        this.purchasedCourses[purchasedCoursesCount++] = course;
    }

    private boolean canGetDiscount() {
        double totalGrades = 0.0;
        int counter = 0;
        for (int i = purchasedCoursesCount - 1; i >= 0; i--) {
            if (purchasedCourses[i].isCompleted()) {
                totalGrades += purchasedCourses[i].getGrade();
                counter++;
            } else {
                break;
            }

        }
        return ((totalGrades / CONSECUTIVE_COURSES_FOR_DISCOUNT) >= MINIMUM_GRADE_FOR_DISCOUNT)
                && counter == CONSECUTIVE_COURSES_FOR_DISCOUNT;
    }
}
