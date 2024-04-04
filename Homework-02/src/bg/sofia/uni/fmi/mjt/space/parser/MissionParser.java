package bg.sofia.uni.fmi.mjt.space.parser;

import bg.sofia.uni.fmi.mjt.space.mission.Detail;
import bg.sofia.uni.fmi.mjt.space.mission.MissionStatus;
import bg.sofia.uni.fmi.mjt.space.rocket.RocketStatus;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class MissionParser {
    public static LocalDate parseDate(String date) {
        DateTimeFormatter format = DateTimeFormatter.ofPattern("E MMM dd, yyyy");
        return LocalDate.parse(date, format);
    }

    public static Detail parseDetail(String detail) {
        String detailWithoutQuotes = detail.replace("\"", "");
        String[] detailTokens = detailWithoutQuotes.split("\\|");
        String token1 = detailTokens[0].trim();
        String token2 = detailTokens[1].trim();
        return new Detail(token1, token2);
    }

    public static RocketStatus parseRocketStatus(String rocketStatus) {
        String rocketStatusWithoutQuotes = rocketStatus.replace("\"", "");
        String[] tokens = rocketStatusWithoutQuotes.split("(?=\\p{Upper})");
        String status = (tokens[0] + "_" + tokens[1]).toUpperCase();
        return RocketStatus.valueOf(status);
    }

    public static Optional<Double> parseCost(String cost) {
        Optional<Double> optCost;
        if (cost.isEmpty()) {
            optCost = Optional.empty();
        } else {
            optCost = Optional.of(Double.parseDouble(cost.replace(",", "")));
        }
        return optCost;
    }

    public static MissionStatus parseMissionStatus(String missionStatus) {
        String missionStatusWithoutQuotes = missionStatus.replace("\"", "");
        for (MissionStatus status : MissionStatus.values()) {
            if (status.toString().equalsIgnoreCase(missionStatusWithoutQuotes)) {
                return status;
            }
        }
        throw new IllegalArgumentException("No enum constant with value " + missionStatus);
    }
}
