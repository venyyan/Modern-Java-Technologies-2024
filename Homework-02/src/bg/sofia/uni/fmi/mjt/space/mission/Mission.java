package bg.sofia.uni.fmi.mjt.space.mission;

import bg.sofia.uni.fmi.mjt.space.parser.MissionParser;
import bg.sofia.uni.fmi.mjt.space.rocket.RocketStatus;

import java.time.LocalDate;
import java.util.Optional;

import static bg.sofia.uni.fmi.mjt.space.parser.LineParser.parse;

public record Mission(String id, String company, String location, LocalDate date,
                      Detail detail, RocketStatus rocketStatus, Optional<Double> cost,
                      MissionStatus missionStatus) {

    public static Mission of(String line) {
        final String[] tokens = parse(line);
        String id = tokens[MissionToken.ID.getTokenValue()];
        String company = tokens[MissionToken.COMPANY.getTokenValue()];
        String location = tokens[MissionToken.LOCATION.getTokenValue()];
        LocalDate date = MissionParser.parseDate(tokens[MissionToken.DATE.getTokenValue()]);
        Detail detail = MissionParser.parseDetail(tokens[MissionToken.DETAIL.getTokenValue()]);
        RocketStatus rocketStatus = MissionParser.parseRocketStatus(tokens[MissionToken.ROCKET_STATUS.getTokenValue()]);
        Optional<Double> cost = MissionParser.parseCost(tokens[MissionToken.COST.getTokenValue()]);
        MissionStatus missionStatus = MissionParser
            .parseMissionStatus(tokens[MissionToken.MISSION_STATUS.getTokenValue()]);

        return new Mission(id, company, location, date, detail, rocketStatus, cost, missionStatus);
    }
}
