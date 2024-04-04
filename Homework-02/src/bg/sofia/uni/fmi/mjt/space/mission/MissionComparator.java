package bg.sofia.uni.fmi.mjt.space.mission;

import java.util.Comparator;

public class MissionComparator implements Comparator<Mission> {
    @Override
    public int compare(Mission mission1, Mission mission2) {
        return mission1.id().compareTo(mission2.id());
    }
}
