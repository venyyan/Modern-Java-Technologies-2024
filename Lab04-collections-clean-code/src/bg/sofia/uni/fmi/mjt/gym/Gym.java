package bg.sofia.uni.fmi.mjt.gym;

import bg.sofia.uni.fmi.mjt.gym.member.Address;
import bg.sofia.uni.fmi.mjt.gym.member.GymMember;
import bg.sofia.uni.fmi.mjt.gym.workout.Exercise;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

public class Gym implements GymAPI {
    private int capacity;
    private Address address;

    private SortedSet<GymMember> members;
    public Gym(int capacity, Address address) {
        this.capacity = capacity;
        this.address = address;
        members = new TreeSet<>();
    }

    @Override
    public SortedSet<GymMember> getMembers() {
        return this.members;
    }

    @Override
    public SortedSet<GymMember> getMembersSortedByName() {
        return this.members;
    }

    @Override
    public SortedSet<GymMember> getMembersSortedByProximityToGym() {
        SortedSet<GymMember> memberSortedSet = new TreeSet<>(new MemberComparator());
        memberSortedSet.addAll(members);
        return memberSortedSet;
    }

    @Override
    public void addMember(GymMember member) throws GymCapacityExceededException {
        if (member == null) {
            throw new IllegalArgumentException("Member is null!");
        }

        if (this.members.size() >= this.capacity) {
            throw new GymCapacityExceededException("Gym capacity is " + this.capacity
                + ". You can't add more members!");
        }

        this.members.add(member);
    }

    @Override
    public void addMembers(Collection<GymMember> members) throws GymCapacityExceededException {
        if (members == null || members.isEmpty()) {
            throw new IllegalArgumentException("Members collection is null!");
        }

        if (this.members.size() + members.size() >= this.capacity) {
            throw new GymCapacityExceededException("Gym capacity is " + this.capacity
                + ". You can't add more members!");
        }
        this.members.addAll(members);
    }

    @Override
    public boolean isMember(GymMember member) {
        if (member == null) {
            throw new IllegalArgumentException("Member is null!");
        }

        return this.members.contains(member);
    }

    @Override
    public boolean isExerciseTrainedOnDay(String exerciseName, DayOfWeek day) {
        if (exerciseName == null || exerciseName.isEmpty()) {
            throw new IllegalArgumentException("Exercise name is null or empty!");
        }

        if (day == null) {
            throw new IllegalArgumentException("Day is null!");
        }

        for (GymMember member : members) {
            if (member.getTrainingProgram().get(day) != null) {
                for (Exercise exercise : member.getTrainingProgram().get(day).exercises()) {
                    if (exercise.name().equals(exerciseName)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public Map<DayOfWeek, List<String>> getDailyListOfMembersForExercise(String exerciseName) {
        if (exerciseName == null || exerciseName.isEmpty()) {
            throw new IllegalArgumentException("Exercise name is null or empty!");
        }

        Map<DayOfWeek, List<String>> listOfMembers = new HashMap<>();
        for (GymMember member : members) {
            for (DayOfWeek dayOfWeek : DayOfWeek.values()) {
                if (member.getTrainingProgram().get(dayOfWeek) != null) {
                    for (Exercise exercise : member.getTrainingProgram().get(dayOfWeek).exercises()) {
                        if (exercise.name().equals(exerciseName)) {
                            listOfMembers.putIfAbsent(dayOfWeek, new ArrayList<>());
                            listOfMembers.get(dayOfWeek).add(member.getName());
                        }
                    }
                }
            }
        }
        return listOfMembers;
    }

    private class MemberComparator implements Comparator<GymMember> {

        @Override
        public int compare(GymMember o1, GymMember o2) {
            if (o1.getAddress().getDistanceTo(address) == o2.getAddress().getDistanceTo(address)) {
                return 0;
            } else if (o1.getAddress().getDistanceTo(address) >= o2.getAddress().getDistanceTo(address)) {
                return 1;
            } else {
                return -1;
            }
        }
    }
}