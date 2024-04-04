package bg.sofia.uni.fmi.mjt.space;

import bg.sofia.uni.fmi.mjt.space.algorithm.Rijndael;
import bg.sofia.uni.fmi.mjt.space.algorithm.SymmetricBlockCipher;
import bg.sofia.uni.fmi.mjt.space.exception.CipherException;
import bg.sofia.uni.fmi.mjt.space.exception.TimeFrameMismatchException;
import bg.sofia.uni.fmi.mjt.space.mission.Mission;
import bg.sofia.uni.fmi.mjt.space.mission.MissionComparator;
import bg.sofia.uni.fmi.mjt.space.mission.MissionStatus;
import bg.sofia.uni.fmi.mjt.space.rocket.Rocket;
import bg.sofia.uni.fmi.mjt.space.rocket.RocketStatus;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MJTSpaceScannerTest {
    static MJTSpaceScanner mjtSpaceScannerAdvanced;
    static MJTSpaceScanner mjtSpaceScannerEmpty;
    static Mission mission1;
    static Mission mission2;
    static Mission mission3;
    static Mission mission4;
    static Mission mission5;
    static Mission mission6;
    static Mission mission7;
    static SecretKey key;

    private static final int KEY_SIZE = 128;
    private static final String ENCRYPTION_ALGORITHM = "AES";
    private static SecretKey generateSecretKey() throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(ENCRYPTION_ALGORITHM);
        keyGenerator.init(KEY_SIZE);
        SecretKey secretKey = keyGenerator.generateKey();

        String base64Key = Base64.getEncoder().encodeToString(secretKey.getEncoded());
        System.out.println("Generated Secret Key (Base64-encoded): " + base64Key);

        return secretKey;
    }

    @BeforeAll
    static void createReader() throws NoSuchAlgorithmException {
        Reader missionsReaderAdvanced = new StringReader("""
            Unnamed: 0,Company Name,Location,Datum,Detail,Status Rocket," Rocket",Status Mission
            0,SpaceX,"LC-39A, Kennedy Space Center, Florida, USA","Fri Aug 07, 2020",Falcon 9 Block 5 | Starlink V1 L9 & BlackSky,StatusActive,"50.0 ",Success
            15,Rocket Lab,"Rocket Lab LC-1A, M?hia Peninsula, New Zealand","Sat Jul 04, 2020",Electron/Curie | Pics Or It Didn??¦t Happen,StatusActive,"7.5 ",Failure
            2,SpaceX,"Pad A, Boca Chica, Texas, USA","Tue Aug 04, 2020",Starship Prototype | 150 Meter Hop,StatusActive,,Success
            3,Roscosmos,"Site 200/39, Baikonur Cosmodrome, Kazakhstan","Thu Jul 30, 2020",Proton-M/Briz-M | Ekspress-80 & Ekspress-103,StatusActive,"65.0 ",Success
            8,SpaceX,"SLC-40, Cape Canaveral AFS, Florida, USA","Mon Jul 20, 2020",Falcon 9 Block 5 | ANASIS-II,StatusActive,"50.0 ",Success
            4,ULA,"SLC-41, Cape Canaveral AFS, Florida, USA","Thu Jul 30, 2020",Atlas V 541 | Perseverance,StatusActive,"145.0 ",Success
            17,SpaceX,"SLC-40, Cape Canaveral AFS, Florida, USA","Tue Jun 30, 2020",Falcon 9 Block 5 | GPS III SV03,StatusActive,"50.0 ",Success
            """);
        Reader rocketsReaderAdvanced = new StringReader("""
            "",Name,Wiki,Rocket Height
            0,Tsyklon-3,https://en.wikipedia.org/wiki/Tsyklon-3,39.0 m
            1,Tsyklon-4M,https://en.wikipedia.org/wiki/Cyclone-4M,38.7 m
            2,Unha-2,https://en.wikipedia.org/wiki/Unha,28.0 m
            3,Unha-3,https://en.wikipedia.org/wiki/Unha,32.0 m
            4,Vanguard,https://en.wikipedia.org/wiki/Vanguard_(rocket),23.0 m
            5,Vector-H,https://en.wikipedia.org/wiki/Vector-H,18.3 m
            62,Atlas-E/F Burner,,
            103,Atlas V 541,https://en.wikipedia.org/wiki/Atlas_V,62.2 m
            294,Proton-M/Briz-M,https://en.wikipedia.org/wiki/Proton-M,58.2 m
            """);
        key = generateSecretKey();
        mjtSpaceScannerAdvanced = new MJTSpaceScanner(missionsReaderAdvanced, rocketsReaderAdvanced, key);

        mission1 = Mission.of("0,SpaceX,\"LC-39A, Kennedy Space Center, Florida, USA\",\"Fri Aug 07, 2020\",Falcon 9 Block 5 | Starlink V1 L9 & BlackSky,StatusActive,\"50.0 \",Success");
        mission2 = Mission.of("2,SpaceX,\"Pad A, Boca Chica, Texas, USA\",\"Tue Aug 04, 2020\",Starship Prototype | 150 Meter Hop,StatusActive,,Success");
        mission3  = Mission.of("3,Roscosmos,\"Site 200/39, Baikonur Cosmodrome, Kazakhstan\",\"Thu Jul 30, 2020\",Proton-M/Briz-M | Ekspress-80 & Ekspress-103,StatusActive,\"65.0 \",Success");
        mission4 = Mission.of("8,SpaceX,\"SLC-40, Cape Canaveral AFS, Florida, USA\",\"Mon Jul 20, 2020\",Falcon 9 Block 5 | ANASIS-II,StatusActive,\"50.0 \",Success");
        mission5  = Mission.of("15,Rocket Lab,\"Rocket Lab LC-1A, M?\u0081hia Peninsula, New Zealand\",\"Sat Jul 04, 2020\",Electron/Curie | Pics Or It Didn??¦t Happen,StatusActive,\"7.5 \",Failure");
        mission6 = Mission.of("4,ULA,\"SLC-41, Cape Canaveral AFS, Florida, USA\",\"Thu Jul 30, 2020\",Atlas V 541 | Perseverance,StatusActive,\"145.0 \",Success");
        mission7 = Mission.of("17,SpaceX,\"SLC-40, Cape Canaveral AFS, Florida, USA\",\"Tue Jun 30, 2020\",Falcon 9 Block 5 | GPS III SV03,StatusActive,\"50.0 \",Success");

        Reader missionsReaderEmpty = new StringReader("""
            Unnamed: 0,Company Name,Location,Datum,Detail,Status Rocket," Rocket",Status Mission
            """);
        Reader rocketsReaderEmpty = new StringReader("""
            "",Name,Wiki,Rocket Height
            """);

        mjtSpaceScannerEmpty = new MJTSpaceScanner(missionsReaderEmpty, rocketsReaderEmpty, key);
    }

    @Test
    void testGetAllMissionsNoMissions() {
        List<Mission> missionsListEmpty = new ArrayList<>();

        assertTrue(missionsListEmpty.size() == mjtSpaceScannerEmpty.getAllMissions().size()
            && missionsListEmpty.containsAll(mjtSpaceScannerEmpty.getAllMissions())
            && mjtSpaceScannerEmpty.getAllMissions().containsAll(missionsListEmpty),
            "It is expected to return empty mission list when using empty scanner. " +
                "Expected list size: 0, but was: " + mjtSpaceScannerEmpty.getAllMissions().size());
    }

    @Test
    void testGetAllMissions() {
        List<Mission> missionsList = List.of(mission5, mission1, mission2, mission3, mission4, mission6, mission7);
        assertTrue(missionsList.size() == mjtSpaceScannerAdvanced.getAllMissions().size()
                && missionsList.containsAll(mjtSpaceScannerAdvanced.getAllMissions())
                && mjtSpaceScannerAdvanced.getAllMissions().containsAll(missionsList),
            "Expected list of all missions: " + missionsList + ", but was: " + mjtSpaceScannerAdvanced.getAllMissions());
    }

    @Test
    void testGetAllMissionsWithNullMissionStatus() {
        assertThrows(IllegalArgumentException.class, () -> mjtSpaceScannerAdvanced.getAllMissions(null),
            "IllegalArgumentException expected to be thrown when trying to get all missions with null mission" +
                "status");
    }

    @Test
    void testGetAllMissionsWithMissionStatusNoMissions() {
        List<Mission> missionsListEmpty = new ArrayList<>();
        assertIterableEquals(missionsListEmpty, mjtSpaceScannerEmpty.getAllMissions(MissionStatus.SUCCESS),
            "It is expected to return empty mission list when using empty scanner." +
                "Expected list size: 0, but was: " + mjtSpaceScannerEmpty.getAllMissions(MissionStatus.SUCCESS));
    }

    @Test
    void testGetAllMissionsWithNonExistingStatus() {
        List<Mission> missionsListEmpty = new ArrayList<>();
        assertIterableEquals(missionsListEmpty, mjtSpaceScannerEmpty.getAllMissions(MissionStatus.PRELAUNCH_FAILURE),
            "Expected list of all missions: " + missionsListEmpty + ", but was: " + mjtSpaceScannerEmpty.getAllMissions());
    }

    @Test
    void testGetAllMissionsWithMissionStatus() {
        List<Mission> missionListWithMissionStatus = List.of(mission2, mission1, mission3, mission4, mission6, mission7);

        assertTrue(missionListWithMissionStatus.size() == mjtSpaceScannerAdvanced.getAllMissions(MissionStatus.SUCCESS).size()
                && missionListWithMissionStatus.containsAll(mjtSpaceScannerAdvanced.getAllMissions(MissionStatus.SUCCESS))
                && mjtSpaceScannerAdvanced.getAllMissions(MissionStatus.SUCCESS).containsAll(missionListWithMissionStatus),
            "Expected list of all missions: " + missionListWithMissionStatus + ", but was: " + mjtSpaceScannerAdvanced.getAllMissions(MissionStatus.SUCCESS));
    }

    @Test
    void testGetCompanyWithMostSuccessfulMissionsWithNullFrom() {
        assertThrows(IllegalArgumentException.class,
            () -> mjtSpaceScannerAdvanced.getCompanyWithMostSuccessfulMissions(null, LocalDate.of(2023, 12, 25)),
            "IllegalArgumentException expected to be thrown when trying to get company with most successful missions" +
                " with null from.");
    }

    @Test
    void testGetCompanyWithMostSuccessfulMissionsWithNullTo() {
        assertThrows(IllegalArgumentException.class,
            () -> mjtSpaceScannerAdvanced.getCompanyWithMostSuccessfulMissions(LocalDate.of(2023, 12, 25), null),
            "IllegalArgumentException expected to be thrown when trying to get company with most successful missions" +
                " with null to.");
    }

    @Test
    void testGetCompanyWithMostSuccessfulMissionsWithToBeforeFrom() {
        assertThrows(TimeFrameMismatchException.class,
            () -> mjtSpaceScannerAdvanced.getCompanyWithMostSuccessfulMissions(
                LocalDate.of(2023, 7, 8), LocalDate.of(2023, 5, 2)),
            "TimeFrameMismatchException expected to be thrown when trying to get company with most successful missions" +
                " with to before from.");
    }

    @Test
    void testGetCompanyWithMostSuccessfulMissions() {
        String actual = mjtSpaceScannerAdvanced.getCompanyWithMostSuccessfulMissions(
            LocalDate.of(2020, 7, 1), LocalDate.of(2023, 7, 29));
        assertEquals("SpaceX", actual,
            "Expected company with most successful missions: SpaceX, but actual was: " + actual);
    }

    @Test
    void testGetCompanyWithMostSuccessfulMissionsNothingFound() {
        String actual = mjtSpaceScannerAdvanced.getCompanyWithMostSuccessfulMissions(
            LocalDate.of(2024, 7, 1), LocalDate.of(2025, 8, 10));
        assertEquals("", actual,
            "It was expected to return empty string when a company was not found, but received: "
        + actual);
    }

    private boolean areEqualIgnoringOrder(Map<String, Collection<Mission>> map1, Map<String, Collection<Mission>> map2) {
        if (map1.size() != map2.size()) {
            return false;
        }

        for (Map.Entry<String, Collection<Mission>> entry : map1.entrySet()) {
            String key = entry.getKey();
            Collection<Mission> collection1 = entry.getValue();
            Collection<Mission> collection2 = map2.get(key);

            if (collection2 == null) {
                return false;
            }

            List<Mission> list1 = new ArrayList<>(collection1);
            List<Mission> list2 = new ArrayList<>(collection2);

            list1.sort(new MissionComparator());
            list2.sort(new MissionComparator());

            if (!list1.equals(list2)) {
                return false;
            }
        }
        return true;
    }
    @Test
    void testGetMissionsPerCountry() {
        Map<String, Collection<Mission>> expectedMissionsPerCountry = new HashMap<>();
        expectedMissionsPerCountry.put("Kazakhstan", List.of(mission3));
        expectedMissionsPerCountry.put("New Zealand", List.of(mission5));
        expectedMissionsPerCountry.put("USA", List.of(mission2, mission1, mission4, mission6, mission7));

        assertTrue(areEqualIgnoringOrder(expectedMissionsPerCountry, mjtSpaceScannerAdvanced.getMissionsPerCountry()
        ), "Expected map of all missions per country: " + expectedMissionsPerCountry + ", but was: " + mjtSpaceScannerAdvanced.getMissionsPerCountry());
    }

    @Test
    void testGetMissionsPerCountryWithNoMissions() {
        Map<String, Collection<Mission>> expectedMissionsPerCountryEmpty = new HashMap<>();
        assertTrue(areEqualIgnoringOrder(expectedMissionsPerCountryEmpty, mjtSpaceScannerEmpty.getMissionsPerCountry()),
            "It is expected to return empty mission list when using empty scanner." +
                "Expected list size: 0, but was: " + expectedMissionsPerCountryEmpty.size());
    }

    @Test
    void testGetTopNLeastExpensiveMissionsWithNegativeN() {
        assertThrows(IllegalArgumentException.class,
            () -> mjtSpaceScannerAdvanced.getTopNLeastExpensiveMissions(-7, MissionStatus.SUCCESS, RocketStatus.STATUS_ACTIVE),
            "IllegalArgumentException expected to be thrown when trying to get top n least expensive missions" +
                " with negative or equal to zero n");
    }

    @Test
    void testGetTopNLeastExpensiveMissionsWithNullMissionStatus() {
        assertThrows(IllegalArgumentException.class,
            () -> mjtSpaceScannerAdvanced.getTopNLeastExpensiveMissions(10, null, RocketStatus.STATUS_ACTIVE),
            "IllegalArgumentException expected to be thrown when trying to get top n least expensive missions" +
                " with null mission status");
    }

    @Test
    void testGetTopNLeastExpensiveMissionsWithNullRocketStatus() {
        assertThrows(IllegalArgumentException.class,
            () -> mjtSpaceScannerAdvanced.getTopNLeastExpensiveMissions(10, MissionStatus.SUCCESS, null),
            "IllegalArgumentException expected to be thrown when trying to get top n least expensive missions" +
                " with null rocket status");
    }

    @Test
    void testGetTopNLeastExpensiveMissions() {
        List<Mission> expectedTopNLeastExpensiveMissionsList = List.of(mission1, mission4, mission7, mission3, mission6);
        List<Mission> actual =  mjtSpaceScannerAdvanced.getTopNLeastExpensiveMissions(5, MissionStatus.SUCCESS,
            RocketStatus.STATUS_ACTIVE);

        assertTrue(expectedTopNLeastExpensiveMissionsList.size() == actual.size()
                && expectedTopNLeastExpensiveMissionsList.containsAll(actual)
                && actual.containsAll(expectedTopNLeastExpensiveMissionsList),
            "Expected list of top n least expensive missions: " + expectedTopNLeastExpensiveMissionsList +
                ", but was: " + actual);
    }

    @Test
    void testGetAllRocketsEmpty() {
        List<Rocket> rocketList = new ArrayList<>();
        assertIterableEquals(rocketList, mjtSpaceScannerEmpty.getAllRockets(),
            "It is expected to return empty rocket list when using empty scanner." +
                "Expected list size: 0, but was: " + mjtSpaceScannerEmpty.getAllRockets().size());
    }

    @Test
    void testGetAllRockets() {
        List<Rocket> rocketList = List.of(
            Rocket.of("0,Tsyklon-3,https://en.wikipedia.org/wiki/Tsyklon-3,39.0 m"),
            Rocket.of("1,Tsyklon-4M,https://en.wikipedia.org/wiki/Cyclone-4M,38.7 m"),
            Rocket.of("2,Unha-2,https://en.wikipedia.org/wiki/Unha,28.0 m"),
            Rocket.of("3,Unha-3,https://en.wikipedia.org/wiki/Unha,32.0 m"),
            Rocket.of("4,Vanguard,https://en.wikipedia.org/wiki/Vanguard_(rocket),23.0 m"),
            Rocket.of("5,Vector-H,https://en.wikipedia.org/wiki/Vector-H,18.3 m"),
            Rocket.of("62,Atlas-E/F Burner,,"),
            Rocket.of("103,Atlas V 541,https://en.wikipedia.org/wiki/Atlas_V,62.2 m"),
            Rocket.of("294,Proton-M/Briz-M,https://en.wikipedia.org/wiki/Proton-M,58.2 m")
        );
        assertTrue(rocketList.size() == mjtSpaceScannerAdvanced.getAllRockets().size()
                && rocketList.containsAll(mjtSpaceScannerAdvanced.getAllRockets())
                && mjtSpaceScannerAdvanced.getAllRockets().containsAll(rocketList),
            "Expected list of top n least expensive missions: " + rocketList +
                ", but was: " + mjtSpaceScannerAdvanced.getAllRockets());
    }

    @Test
    void testGetTopNTallestRocketsWithNegativeN() {
        assertThrows(IllegalArgumentException.class, () -> mjtSpaceScannerAdvanced.getTopNTallestRockets(-7),
            "IllegalArgumentException expected to be thrown when trying to get top n tallest rockets" +
                " with negative n");
    }

    @Test
    void testGetTopNTallestRockets() {
        List<Rocket> topNTallestRocketsList = List.of(
            Rocket.of("103,Atlas V 541,https://en.wikipedia.org/wiki/Atlas_V,62.2 m"),
            Rocket.of("294,Proton-M/Briz-M,https://en.wikipedia.org/wiki/Proton-M,58.2 m"),
            Rocket.of("0,Tsyklon-3,https://en.wikipedia.org/wiki/Tsyklon-3,39.0 m"),
            Rocket.of("1,Tsyklon-4M,https://en.wikipedia.org/wiki/Cyclone-4M,38.7 m")
        );
        assertTrue(topNTallestRocketsList.size() == mjtSpaceScannerAdvanced.getTopNTallestRockets(4).size()
                && topNTallestRocketsList.containsAll(mjtSpaceScannerAdvanced.getTopNTallestRockets(4))
                && mjtSpaceScannerAdvanced.getTopNTallestRockets(4).containsAll(topNTallestRocketsList),
            "Expected list of top n least expensive missions: " + topNTallestRocketsList +
                ", but was: " + mjtSpaceScannerAdvanced.getTopNTallestRockets(4));
    }

    @Test
    void testGetWikiPageForRocket() {
        Map<String, Optional<String>> expectedWikiPageForRocket = new HashMap<>();
        expectedWikiPageForRocket.put("Tsyklon-3", Optional.of("https://en.wikipedia.org/wiki/Tsyklon-3"));
        expectedWikiPageForRocket.put("Tsyklon-4M", Optional.of("https://en.wikipedia.org/wiki/Cyclone-4M"));
        expectedWikiPageForRocket.put("Unha-3", Optional.of("https://en.wikipedia.org/wiki/Unha"));
        expectedWikiPageForRocket.put("Unha-2", Optional.of("https://en.wikipedia.org/wiki/Unha"));
        expectedWikiPageForRocket.put("Vector-H", Optional.of("https://en.wikipedia.org/wiki/Vector-H"));
        expectedWikiPageForRocket.put("Atlas V 541", Optional.of("https://en.wikipedia.org/wiki/Atlas_V"));
        expectedWikiPageForRocket.put("Proton-M/Briz-M", Optional.of("https://en.wikipedia.org/wiki/Proton-M"));
        expectedWikiPageForRocket.put("Vanguard", Optional.of("https://en.wikipedia.org/wiki/Vanguard_(rocket)"));

        assertEquals(expectedWikiPageForRocket, mjtSpaceScannerAdvanced.getWikiPageForRocket(),
            "Expected map of wiki pages per country: " + expectedWikiPageForRocket + ", but was: " + mjtSpaceScannerAdvanced.getWikiPageForRocket());
    }

    @Test
    void testGetWikiPageForRocketNoRockets() {
        Map<String, Optional<String>> expectedWikiPageForRocketEmpty = new HashMap<>();
        assertEquals(expectedWikiPageForRocketEmpty, mjtSpaceScannerEmpty.getWikiPageForRocket(),
            "It is expected to return empty map with wikis of rockets when using empty scanner." +
                "Expected map size: 0, but was: " + mjtSpaceScannerEmpty.getWikiPageForRocket().size());
    }

    @Test
    void testGetMostDesiredLocationForMissionsPerCompany() {
        Map<String, String> mostDesiredLocationForMissions = new HashMap<>();
        mostDesiredLocationForMissions.put("SpaceX", "SLC-40, Cape Canaveral AFS, Florida, USA");
        mostDesiredLocationForMissions.put("Rocket Lab", "Rocket Lab LC-1A, M?hia Peninsula, New Zealand");
        mostDesiredLocationForMissions.put("ULA", "SLC-41, Cape Canaveral AFS, Florida, USA");
        mostDesiredLocationForMissions.put("Roscosmos", "Site 200/39, Baikonur Cosmodrome, Kazakhstan");

        assertEquals(mostDesiredLocationForMissions,
            mjtSpaceScannerAdvanced.getMostDesiredLocationForMissionsPerCompany(),
            "Expected map of most desired location for missions: " + mostDesiredLocationForMissions +
                ", but was: " + mjtSpaceScannerAdvanced.getMostDesiredLocationForMissionsPerCompany());
    }

    @Test
    void testGetLocationWithMostSuccessfulMissionsPerCompanyWithNullFrom() {
        assertThrows(IllegalArgumentException.class,
            () -> mjtSpaceScannerAdvanced.getLocationWithMostSuccessfulMissionsPerCompany(null, LocalDate.of(2020, 5, 10)),
            "IllegalArgumentException expected to be thrown when trying to get company with most successful missions" +
                " with null from.");
    }

    @Test
    void testGetLocationWithMostSuccessfulMissionsPerCompanyWithNullTo() {
        assertThrows(IllegalArgumentException.class,
            () -> mjtSpaceScannerAdvanced.getLocationWithMostSuccessfulMissionsPerCompany(LocalDate.of(2020, 5, 10), null),
            "IllegalArgumentException expected to be thrown when trying to get company with most successful missions" +
                " with null to.");
    }

    @Test
    void testGetLocationWithMostSuccessfulMissionsPerCompanyWithFromBeforeTo() {
        assertThrows(TimeFrameMismatchException.class,
            () -> mjtSpaceScannerAdvanced.getLocationWithMostSuccessfulMissionsPerCompany(
                LocalDate.of(2020, 6, 20), LocalDate.of(2020, 5, 10)),
            "TimeFrameMismatchException expected to be thrown when trying to get company with most successful missions" +
                " with to before from.");
    }

    @Test
    void testGetLocationWithMostSuccessfulMissionsPerCompany() {
        Map<String, String> companiesToLocationWithMostSuccesfullMissions = new HashMap<>();
        companiesToLocationWithMostSuccesfullMissions.put("SpaceX", "SLC-40, Cape Canaveral AFS, Florida, USA");
        companiesToLocationWithMostSuccesfullMissions.put("Roscosmos", "Site 200/39, Baikonur Cosmodrome, Kazakhstan");
        companiesToLocationWithMostSuccesfullMissions.put("ULA", "SLC-41, Cape Canaveral AFS, Florida, USA");

        Map<String, String> actual = mjtSpaceScannerAdvanced.getLocationWithMostSuccessfulMissionsPerCompany(
            LocalDate.of(1, 1, 1), LocalDate.of(2070, 12, 30));
        assertEquals(companiesToLocationWithMostSuccesfullMissions, actual,
            "Expected map of companies to locations with most successful missions: " + companiesToLocationWithMostSuccesfullMissions +
                ", but was: " + actual);
    }

    @Test
    void testGetWikiPagesForRocketsUsedInMostExpensiveMissionsWithNegativeN() {
        assertThrows(IllegalArgumentException.class,
            () -> mjtSpaceScannerAdvanced.getWikiPagesForRocketsUsedInMostExpensiveMissions(-7, MissionStatus.SUCCESS,
                RocketStatus.STATUS_ACTIVE),
            "IllegalArgumentException expected to be thrown when trying to get top n wiki pages for " +
                "rockets used in most expensive missions with negative n");
    }

    @Test
    void testGetWikiPagesForRocketsUsedInMostExpensiveMissionsWithNullMissionStatus() {
        assertThrows(IllegalArgumentException.class,
            () -> mjtSpaceScannerAdvanced.getWikiPagesForRocketsUsedInMostExpensiveMissions(10, null, RocketStatus.STATUS_RETIRED),
            "IllegalArgumentException expected to be thrown when trying to get " +
                "wiki pages for rockets used in most expensive missions status with null mission status");
    }

    @Test
    void testGetWikiPagesForRocketsUsedInMostExpensiveMissionsWithNullRocketStatus() {
        assertThrows(IllegalArgumentException.class,
            () -> mjtSpaceScannerAdvanced.getWikiPagesForRocketsUsedInMostExpensiveMissions(10, MissionStatus.FAILURE, null),
            "IllegalArgumentException expected to be thrown when trying to get " +
                "wiki pages for rockets used in most expensive missions status with null rocket status");
    }

    @Test
    void testGetWikiPagesForRocketsUsedInMostExpensiveMissions() {
        List<String> expectedWikiPages = new ArrayList<>();
        List<String> actual = mjtSpaceScannerAdvanced.getWikiPagesForRocketsUsedInMostExpensiveMissions(1, MissionStatus.SUCCESS,
            RocketStatus.STATUS_ACTIVE);

        assertTrue(expectedWikiPages.size() == actual.size()
                && expectedWikiPages.containsAll(actual)
                && actual.containsAll(expectedWikiPages),
            "Expected list of all missions: " +
                expectedWikiPages + ", but was: " + actual);
    }

    @Test
    void testSaveMostReliableRocketWithNullFrom() {
        assertThrows(IllegalArgumentException.class,
            () -> mjtSpaceScannerAdvanced.saveMostReliableRocket(null, null, LocalDate.of(2003, 8, 10)),
            "IllegalArgumentException expected to be thrown when trying to save most reliable rocket" +
                " with null from");
    }

    @Test
    void testSaveMostReliableRocketWithNullTo() {
        assertThrows(IllegalArgumentException.class,
            () -> mjtSpaceScannerAdvanced.saveMostReliableRocket(null, LocalDate.of(2003, 8, 10), null),
            "IllegalArgumentException expected to be thrown when trying to save most reliable rocket" +
                " with null to");
    }

    @Test
    void testSaveMostReliableRocketWithToBeforeFrom() {
        assertThrows(TimeFrameMismatchException.class,
            () -> mjtSpaceScannerAdvanced.saveMostReliableRocket(null, LocalDate.of(2020, 5, 10),
                LocalDate.of(2019, 5, 5)),
            "TimeFrameMismatchException expected to be thrown when trying to save most reliable rocket" +
                " with to before from.");
    }

    @Test
    void testSaveMostReliableRocketWithNullOS() {
        assertThrows(IllegalArgumentException.class,
            () -> mjtSpaceScannerAdvanced.saveMostReliableRocket(null, LocalDate.of(2020, 5, 10),
                LocalDate.of(2021, 5, 5)),
            "TimeFrameMismatchException expected to be thrown when trying to save most reliable rocket" +
                " with null output stream");
    }
    @Test
    void testSaveMostReliableRocket() {
        String mostReliableRocketName = "Proton-M/Briz-M";
        try (ByteArrayOutputStream saved1 = new ByteArrayOutputStream();
             ByteArrayOutputStream saved2 = new ByteArrayOutputStream()) {

            SymmetricBlockCipher rijndael = new Rijndael(key);
            rijndael.encrypt(new ByteArrayInputStream(mostReliableRocketName.getBytes()), saved1);

            mjtSpaceScannerAdvanced.saveMostReliableRocket(saved2, LocalDate.of(1, 1, 1), LocalDate.of(2070, 1, 1));

            String encryptedString1 = saved1.toString();
            String encryptedString2 = saved2.toString();

            assertEquals(encryptedString1, encryptedString2,
                ("It was expected to save " + saved1 + ", but actual was: " + saved2));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CipherException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testSaveMostReliableRocketNoRocket() {
        String mostReliableRocketName = "";
        try (ByteArrayOutputStream saved1 = new ByteArrayOutputStream();
             ByteArrayOutputStream saved2 = new ByteArrayOutputStream()) {

            SymmetricBlockCipher rijndael = new Rijndael(key);
            rijndael.encrypt(new ByteArrayInputStream(mostReliableRocketName.getBytes()), saved1);

            mjtSpaceScannerAdvanced.saveMostReliableRocket(saved2, LocalDate.of(1, 1, 1), LocalDate.of(1, 1, 1));

            String encryptedString1 = saved1.toString();
            String encryptedString2 = saved2.toString();

            assertEquals(encryptedString1, encryptedString2,
                ("It was expected to save empty string, but was: " + saved2.toString()));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CipherException e) {
            throw new RuntimeException(e);
        }
    }
}
