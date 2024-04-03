package bg.sofia.uni.fmi.mjt.gym.member;

import bg.sofia.uni.fmi.mjt.gym.workout.Exercise;
import bg.sofia.uni.fmi.mjt.gym.workout.Workout;

import java.time.DayOfWeek;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Member implements GymMember, Comparable<Member> {
    private Address address;
    private String name;
    private int age;
    private String personalIdNumber;
    private Gender gender;
    private Map<DayOfWeek, Workout> trainingProgram;

    public Member(Address address, String name, int age, String personalIdNumber, Gender gender) {
        this.address = address;
        this.name = name;
        this.age = age;
        this.personalIdNumber = personalIdNumber;
        this.gender = gender;
        trainingProgram = new HashMap<>();
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public int getAge() {
        return this.age;
    }

    @Override
    public String getPersonalIdNumber() {
        return this.personalIdNumber;
    }

    @Override
    public Gender getGender() {
        return this.gender;
    }

    @Override
    public Address getAddress() {
        return this.address;
    }

    @Override
    public Map<DayOfWeek, Workout> getTrainingProgram() {
        return this.trainingProgram;
    }

    @Override
    public void setWorkout(DayOfWeek day, Workout workout) {
        if (day == null) {
            throw new IllegalArgumentException("Day is null!");
        }

        if (workout == null) {
            throw new IllegalArgumentException("Workout is null!");
        }
        this.trainingProgram.put(day, workout);
    }

    @Override
    public Collection<DayOfWeek> getDaysFinishingWith(String exerciseName) {
        if (exerciseName == null || exerciseName.isEmpty()) {
            throw new IllegalArgumentException("Exercise name is null or empty!");
        }

        Collection<DayOfWeek> daysFinishingWith = new HashSet<>();
        for (DayOfWeek dayOfWeek : DayOfWeek.values()) {
            if (trainingProgram.get(dayOfWeek) != null) {
                for (Exercise exercise : trainingProgram.get(dayOfWeek).exercises()) {
                    if (exercise.name().equals(exerciseName)) {
                        daysFinishingWith.add(dayOfWeek);
                    }
                }
            }
        }
        return daysFinishingWith;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Member member = (Member) o;
        return Objects.equals(personalIdNumber, member.personalIdNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(personalIdNumber);
    }

    @Override
    public void addExercise(DayOfWeek day, Exercise exercise) {
        if (day == null) {
            throw new IllegalArgumentException("Day is null!");
        }

        if (exercise == null) {
            throw new IllegalArgumentException("Exercise is null!");
        }

        if (this.trainingProgram.get(day) == null) {
            throw new DayOffException("Workout on " + day.name() + " is null!");
        }
        this.trainingProgram.get(day).exercises().add(exercise);
    }

    @Override
    public void addExercises(DayOfWeek day, List<Exercise> exercises) {
        if (day == null) {
            throw new IllegalArgumentException("Day is null!");
        }

        if (exercises == null || exercises.isEmpty()) {
            throw new IllegalArgumentException("Exercises is null!");
        }

        if (this.trainingProgram.get(day) == null) {
            throw new DayOffException("Workout on " + day.name() + " is null!");
        }
        this.trainingProgram.get(day).exercises().addAll(exercises);
    }

    @Override
    public int compareTo(Member o) {
        return this.name.compareTo(o.name);
    }
}