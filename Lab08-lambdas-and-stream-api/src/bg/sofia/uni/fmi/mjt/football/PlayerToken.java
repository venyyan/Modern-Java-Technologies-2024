package bg.sofia.uni.fmi.mjt.football;

public enum PlayerToken {
    NAME(0),
    FULL_NAME(1),
    BIRTHDAY(2),
    AGE(3),
    HEIGHT(4),
    WEIGHT(5),
    POSITIONS(6),
    NATIONALITY(7),
    RATING(8),
    POTENTIAL(9),
    VALUE(10),
    WAGE(11),
    FOOT(12);

    private final int tokenValue;

    PlayerToken(int tokenValue) {
        this.tokenValue = tokenValue;
    }

    public int getTokenValue() {
        return tokenValue;
    }
}
