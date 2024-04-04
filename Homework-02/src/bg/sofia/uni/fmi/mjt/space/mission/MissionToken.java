package bg.sofia.uni.fmi.mjt.space.mission;

public enum MissionToken {
    ID(0),
    COMPANY(1),
    LOCATION(2),
    DATE(3),
    DETAIL(4),
    ROCKET_STATUS(5),
    COST(6),
    MISSION_STATUS(7);

    private final int tokenValue;

    MissionToken(int tokenValue) {
        this.tokenValue = tokenValue;
    }

    public int getTokenValue() {
        return tokenValue;
    }
}
