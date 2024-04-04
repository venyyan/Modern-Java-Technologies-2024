package bg.sofia.uni.fmi.mjt.space;

import bg.sofia.uni.fmi.mjt.space.algorithm.Rijndael;
import bg.sofia.uni.fmi.mjt.space.algorithm.SymmetricBlockCipher;
import bg.sofia.uni.fmi.mjt.space.exception.CipherException;
import bg.sofia.uni.fmi.mjt.space.exception.TimeFrameMismatchException;
import bg.sofia.uni.fmi.mjt.space.mission.Mission;
import bg.sofia.uni.fmi.mjt.space.mission.MissionStatus;
import bg.sofia.uni.fmi.mjt.space.rocket.Rocket;
import bg.sofia.uni.fmi.mjt.space.rocket.RocketStatus;

import javax.crypto.SecretKey;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class MJTSpaceScanner implements SpaceScannerAPI {
    private Collection<Mission> missionsList;
    private Collection<Rocket> rocketList;
    private SecretKey secretKey;

    public MJTSpaceScanner(Reader missionsReader, Reader rocketsReader, SecretKey secretKey) {
        try (var buffReaderMissions = new BufferedReader(missionsReader);
             var buffReaderRockets = new BufferedReader(rocketsReader)) {
            missionsList = buffReaderMissions.lines().skip(1).map(Mission::of).toList();
            rocketList = buffReaderRockets.lines().skip(1).map(Rocket::of).toList();
            this.secretKey = secretKey;
        } catch (IOException e) {
            throw new UncheckedIOException("A problem occurred while trying to read from file!", e);
        }
    }

    @Override
    public Collection<Mission> getAllMissions() {
        return missionsList;
    }

    @Override
    public Collection<Mission> getAllMissions(MissionStatus missionStatus) {
        if (missionStatus == null) {
            throw new IllegalArgumentException("Mission status is null!");
        }

        return missionsList.stream()
            .filter(m -> m.missionStatus().equals(missionStatus))
            .toList();
    }

    @Override
    public String getCompanyWithMostSuccessfulMissions(LocalDate from, LocalDate to) {
        if (from == null) {
            throw new IllegalArgumentException("From date is null!");
        }

        if (to == null) {
            throw new IllegalArgumentException("To date is null!");
        }

        if (to.isBefore(from)) {
            throw new TimeFrameMismatchException("To date cannot be before from date!");
        }

        Map<String, Long> successfulMissionsCount = missionsList.stream()
            .filter(m -> m.missionStatus() == MissionStatus.SUCCESS)
            .filter(m -> (m.date().isAfter(from) || m.date().equals(from))
                && (m.date().isBefore(to) || m.date().equals(to)))
            .collect(Collectors.groupingBy(Mission::company, Collectors.counting()));

        return successfulMissionsCount.entrySet().stream()
            .max(Map.Entry.comparingByValue())
            .map(Map.Entry::getKey)
            .orElse("");
    }

    @Override
    public Map<String, Collection<Mission>> getMissionsPerCountry() {
        return missionsList.stream()
            .collect(Collectors.groupingBy(m -> {
                String[] locationTokens = m.location().split(",");
                return locationTokens[locationTokens.length - 1].trim();
            }, Collectors.toCollection(ArrayList::new)));
    }

    private void checkStatus(MissionStatus missionStatus, RocketStatus rocketStatus) {
        if (missionStatus == null) {
            throw new IllegalArgumentException("Mission status cannot be null!");
        }

        if (rocketStatus == null) {
            throw new IllegalArgumentException("Rocket status cannot be null!");
        }
    }

    @Override
    public List<Mission> getTopNLeastExpensiveMissions(int n, MissionStatus missionStatus, RocketStatus rocketStatus) {
        if (n <= 0) {
            throw new IllegalArgumentException("N cannot be equal or less than 0!");
        }

        checkStatus(missionStatus, rocketStatus);

        return missionsList.stream()
            .filter(m -> m.missionStatus().equals(missionStatus) && m.rocketStatus().equals(rocketStatus))
            .filter(m -> m.cost().isPresent())
            .sorted(Comparator.comparingDouble(m -> m.cost().orElse(0.0)))
            .limit(n)
            .toList();
    }

    @Override
    public Map<String, String> getMostDesiredLocationForMissionsPerCompany() {
        Map<String, Map<String, Long>> companiesWithTheCountOfLocations =
            missionsList.stream()
                .collect(Collectors.groupingBy(Mission::company,
                    Collectors.groupingBy(Mission::location, Collectors.counting())));

        return companiesWithTheCountOfLocations.entrySet().stream()
            .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("")));
    }

    @Override
    public Map<String, String> getLocationWithMostSuccessfulMissionsPerCompany(LocalDate from, LocalDate to) {
        if (from == null) {
            throw new IllegalArgumentException("From date is null!");
        }

        if (to == null) {
            throw new IllegalArgumentException("To date is null!");
        }

        if (to.isBefore(from)) {
            throw new TimeFrameMismatchException("To date cannot be before from date!");
        }

        Map<String, Map<String, Long>> companiesWithTheCountOfSuccessfulMissions =
            missionsList.stream()
                .filter(mission -> mission.date().isAfter(from) && mission.date().isBefore(to))
                .filter(mission -> mission.missionStatus() == MissionStatus.SUCCESS)
                .collect(Collectors.groupingBy(Mission::company,
                    Collectors.groupingBy(Mission::location, Collectors.counting())));

        return companiesWithTheCountOfSuccessfulMissions.entrySet().stream()
            .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("")));
    }

    @Override
    public Collection<Rocket> getAllRockets() {
        return rocketList;
    }

    @Override
    public List<Rocket> getTopNTallestRockets(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("N cannot be equal or less than 0!");
        }

        return rocketList.stream()
            .filter(r -> r.height().isPresent())
            .sorted(Comparator.<Rocket, Double>comparing(r -> r.height().orElse(0.0)).reversed())
            .limit(n)
            .toList();
    }

    @Override
    public Map<String, Optional<String>> getWikiPageForRocket() {
        return rocketList.stream()
            .filter(r -> r.wiki().isPresent())
            .collect(Collectors.toMap(Rocket::name, Rocket::wiki));
    }

    @Override
    public List<String> getWikiPagesForRocketsUsedInMostExpensiveMissions(int n, MissionStatus missionStatus,
                                                                          RocketStatus rocketStatus) {
        if (n <= 0) {
            throw new IllegalArgumentException("N cannot be equal or less than 0!");
        }
        checkStatus(missionStatus, rocketStatus);

        List<Mission> mostExpensiveMissions = missionsList.stream()
            .filter(m -> m.missionStatus().equals(missionStatus))
            .filter(m -> m.rocketStatus().equals(rocketStatus))
            .filter(m -> m.cost().isPresent())
            .limit(n)
            .sorted(Comparator.comparingDouble((Mission m) -> m.cost().orElse(0.0)).reversed())
            .toList();

        List<String> rocketsFromMostExpensiveMissions = new ArrayList<>();
        for (Mission mostExpensiveMission : mostExpensiveMissions) {
            rocketsFromMostExpensiveMissions.add(mostExpensiveMission.detail().rocketName());
        }

        return rocketsFromMostExpensiveMissions.stream()
            .map(rocketName ->
                rocketList.stream()
                    .filter(rocket -> rocket.name().equals(rocketName))
                    .findFirst()
                    .flatMap(Rocket::wiki)
                    .orElse(""))
            .limit(n)
            .filter(wiki -> !wiki.isEmpty())
            .toList();
    }

    private Map<String, Double> findRocketToReliabilityMap(List<String> rocketNames, LocalDate from, LocalDate to) {
        return rocketNames.stream()
            .collect(Collectors.toMap(
                rocketName -> rocketName,
                rocketName -> {
                    long successfulMissions = missionsList.stream()
                        .filter(m -> m.date().isBefore(to) && m.date().isAfter(from))
                        .filter(m -> m.detail().rocketName().equals(rocketName))
                        .filter(m -> m.missionStatus().equals(MissionStatus.SUCCESS))
                        .count();
                    long unsuccessfulMissions = missionsList.stream()
                        .filter(m -> m.date().isBefore(to) && m.date().isAfter(from))
                        .filter(m -> m.detail().rocketName().equals(rocketName))
                        .filter(m -> m.missionStatus().equals(MissionStatus.FAILURE))
                        .count();
                    long totalMissions = missionsList.stream()
                        .filter(m -> m.date().isBefore(to) && m.date().isAfter(from))
                        .filter(m -> m.detail().rocketName().equals(rocketName))
                        .count();
                    return (2.0 * successfulMissions + unsuccessfulMissions) / (2.0 * totalMissions);
                }
            ));
    }

    private String findMostReliableRocket(LocalDate from, LocalDate to) {
        List<String> rocketNames = missionsList.stream()
            .map(m -> m.detail().rocketName())
            .distinct()
            .toList();

        Map<String, Double> rocketToReliability = findRocketToReliabilityMap(rocketNames, from, to);

        Optional<Map.Entry<String, Double>> mostReliableRocket = rocketToReliability.entrySet().stream()
            .max(Map.Entry.comparingByValue());

        String mostReliableRocketName = mostReliableRocket.map(Map.Entry::getKey).orElse("");
        Double m = mostReliableRocket.map(Map.Entry::getValue).orElse(0d);

        if (m.isNaN()) {
            mostReliableRocketName = "";
        }
        return mostReliableRocketName;
    }

    @Override
    public void saveMostReliableRocket(OutputStream outputStream, LocalDate from, LocalDate to) throws CipherException {
        if (from == null) {
            throw new IllegalArgumentException("From date is null!");
        }

        if (to == null) {
            throw new IllegalArgumentException("To date is null!");
        }

        if (to.isBefore(from)) {
            throw new TimeFrameMismatchException("To date cannot be before from date!");
        }

        if (outputStream == null) {
            throw new IllegalArgumentException("Output stream is null!");
        }

        String mostReliableRocketName = findMostReliableRocket(from, to);

        try (InputStream inputRocketName = new ByteArrayInputStream(mostReliableRocketName.getBytes())) {
            SymmetricBlockCipher rijndael = new Rijndael(this.secretKey);
            rijndael.encrypt(inputRocketName, outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
