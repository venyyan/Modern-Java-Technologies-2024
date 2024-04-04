package bg.sofia.uni.fmi.mjt.football;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Parser {
    public static LocalDate parseDate(String date) {
        String[] tokens = date.split("/");
        return LocalDate.of(Integer.parseInt(tokens[2]),
                            Integer.parseInt(tokens[0]),
                            Integer.parseInt(tokens[1]));
    }

    public static Foot parseFoot(String foot) {
        return Foot.valueOf(foot.toUpperCase());
    }

    public static List<Position> parseListPositions(String positions) {
        String[] posTokens = positions.split(",");

        List<Position> positionsList = new ArrayList<>();
        for (String pos : posTokens) {
            positionsList.add(Position.valueOf(pos));
        }

        return positionsList;
    }
}
