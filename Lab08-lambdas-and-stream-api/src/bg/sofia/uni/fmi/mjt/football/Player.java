package bg.sofia.uni.fmi.mjt.football;

import java.time.LocalDate;
import java.util.List;

public record Player(String name, String fullName, LocalDate birthDate, int age, double heightCm,
                     double weightKg, List<Position> positions, String nationality,
                     int overallRating, int potential, long valueEuro,
                     long wageEuro, Foot preferredFoot) {

    private static final String PLAYER_ATTRIBUTE_DELIMITER = ";";

    public static Player of(String line) {
        final String[] tokens = line.split(PLAYER_ATTRIBUTE_DELIMITER);
        LocalDate date = Parser.parseDate(tokens[PlayerToken.BIRTHDAY.getTokenValue()]);
        Foot foot = Parser.parseFoot(tokens[PlayerToken.FOOT.getTokenValue()]);
        List<Position> positions = Parser.parseListPositions(tokens[PlayerToken.POSITIONS.getTokenValue()]);

        return new Player(tokens[PlayerToken.NAME.getTokenValue()],
            tokens[PlayerToken.FULL_NAME.getTokenValue()],
            date, Integer.parseInt(tokens[PlayerToken.AGE.getTokenValue()]),
            Double.parseDouble(tokens[PlayerToken.HEIGHT.getTokenValue()]),
            Double.parseDouble(tokens[PlayerToken.WEIGHT.getTokenValue()]), positions,
            tokens[PlayerToken.NATIONALITY.getTokenValue()],
            Integer.parseInt(tokens[PlayerToken.RATING.getTokenValue()]),
            Integer.parseInt(tokens[PlayerToken.POTENTIAL.getTokenValue()]),
            Long.parseLong(tokens[PlayerToken.VALUE.getTokenValue()]),
            Long.parseLong(tokens[PlayerToken.WAGE.getTokenValue()]), foot);
    }
}


