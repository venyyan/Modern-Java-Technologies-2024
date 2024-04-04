package bg.sofia.uni.fmi.mjt.football;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.Reader;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FootballPlayerAnalyzerTest {

    static FootballPlayerAnalyzer footballPlayerAnalyzer;

    @BeforeAll
    static void createReader() {
        Reader basicReader = new StringReader("""
            ime
            A. Paloschi;Alberto Paloschi;1/4/1990;29;182.88;79.8;ST;Italy;76;76;8000000;17000;LEFT
            L. Messi;Lionel Andrés Messi Cuccittini;6/24/1987;31;170.18;72.1;CF,RW,ST;Argentina;94;94;110500000;565000;LEFT
            L. Insigne;Lorenzo Insigne;6/4/1991;27;162.56;59;LW,ST;Italy;88;88;62000000;165000;RIGHT
            A. Kolev;Alexander Kolev;12/8/1992;26;190.5;81.2;ST;Bulgaria;65;68;700000;2000;RIGHT
            B. Kraev;Bozhidar Kraev;6/23/1997;21;185.42;69.9;CM,ST,RW;Bulgaria;62;72;475000;3000;RIGHT""");
        footballPlayerAnalyzer = new FootballPlayerAnalyzer(basicReader);
    }
    @Test
    void testGetHighestPaidPlayerByNationalityWithNullNationality() {
        assertThrows(IllegalArgumentException.class, () -> footballPlayerAnalyzer.getHighestPaidPlayerByNationality(null),
            "IllegalArgumentException expected to be thrown when trying to add null nationality!");
    }

    @Test
    void testGetHighestPaidPlayerByNationalityWithEmptyNationality() {
        assertThrows(IllegalArgumentException.class, () -> footballPlayerAnalyzer.getHighestPaidPlayerByNationality(""),
            "IllegalArgumentException expected to be thrown when trying to add empty nationality!");
    }

    @Test
    void testGetHighestPaidPlayerByNationality() {
        Player expectedPlayer = Player.of("B. Kraev;Bozhidar Kraev;6/23/1997;21;185.42;69.9;CM,ST,RW;Bulgaria;62;72;475000;3000;RIGHT");
        assertEquals(expectedPlayer, footballPlayerAnalyzer.getHighestPaidPlayerByNationality("Bulgaria"));
    }

    @Test
    void testGetAllNationalities() {
        Set<String> expectedNationalities = Set.of("Bulgaria", "Italy", "Argentina");
        assertEquals(expectedNationalities, footballPlayerAnalyzer.getAllNationalities());
    }

    @Test
    void testGetTopProspectPlayerForPositionInBudgetWithNullPositions() {
        assertThrows(IllegalArgumentException.class, () -> footballPlayerAnalyzer.getTopProspectPlayerForPositionInBudget(null, 0),
            "IllegalArgumentException expected to be thrown when trying to use null positions!");
    }

    @Test
    void testGetTopProspectPlayerForPositionInBudgetWithNegativeBudget() {
        assertThrows(IllegalArgumentException.class, () -> footballPlayerAnalyzer.getTopProspectPlayerForPositionInBudget(Position.CB, -10),
            "IllegalArgumentException expected to be thrown when trying to use negative budget!");
    }

    @Test
    void testGetTopProspectPlayerForPositionInBudget() {
        Player expectedPlayer = Player.of("B. Kraev;Bozhidar Kraev;6/23/1997;21;185.42;69.9;CM,ST,RW;Bulgaria;62;72;475000;3000;RIGHT");
        Optional<Player> optionalPlayer = Optional.of(expectedPlayer);
        assertEquals(optionalPlayer, footballPlayerAnalyzer.getTopProspectPlayerForPositionInBudget(Position.RW, 100000000));
    }


    @Test
    void testGetPlayersByFullNameKeywordWithNullKeyword() {
        assertThrows(IllegalArgumentException.class, () -> footballPlayerAnalyzer.getPlayersByFullNameKeyword(null),
            "IllegalArgumentException expected to be thrown when trying to use null keyword!");
    }

    @Test
    void testGetPlayersByFullNameKeyword() {
        Player expectedPlayer1 = Player.of("A. Kolev;Alexander Kolev;12/8/1992;26;190.5;81.2;ST;Bulgaria;65;68;700000;2000;RIGHT");
        Player expectedPlayer2 = Player.of("B. Kraev;Bozhidar Kraev;6/23/1997;21;185.42;69.9;CM,ST,RW;Bulgaria;62;72;475000;3000;RIGHT");
        Set<Player> expectedSet = Set.of(expectedPlayer1, expectedPlayer2);

        assertEquals(expectedSet, footballPlayerAnalyzer.getPlayersByFullNameKeyword("ev"));
    }

    @Test
    void testGetSimilarPlayersWithNullPlayer() {
        assertThrows(IllegalArgumentException.class, () -> footballPlayerAnalyzer.getSimilarPlayers(null),
            "IllegalArgumentException expected to be thrown when trying to use null player!");
    }

    @Test
    void testGetSimilarPlayers() {
        Player expectedPlayer1 = Player.of("A. Kolev;Alexander Kolev;12/8/1992;26;190.5;81.2;ST;Bulgaria;65;68;700000;2000;RIGHT");
        Player expectedPlayer2 = Player.of("B. Kraev;Bozhidar Kraev;6/23/1997;21;185.42;69.9;CM,ST,RW;Bulgaria;62;72;475000;3000;RIGHT");
        Set<Player> expectedSet = Set.of(expectedPlayer1, expectedPlayer2);

        assertEquals(expectedSet, footballPlayerAnalyzer.getSimilarPlayers(expectedPlayer1));
    }

    @Test
    void testGroupByPosition() {
        Player player1 = Player.of("A. Kolev;Alexander Kolev;12/8/1992;26;190.5;81.2;ST;Bulgaria;65;68;700000;2000;RIGHT");
        Player player2 = Player.of("B. Kraev;Bozhidar Kraev;6/23/1997;21;185.42;69.9;CM,ST,RW;Bulgaria;62;72;475000;3000;RIGHT");
        Player player3 = Player.of("A. Paloschi;Alberto Paloschi;1/4/1990;29;182.88;79.8;ST;Italy;76;76;8000000;17000;LEFT");
        Player player4 = Player.of("L. Messi;Lionel Andrés Messi Cuccittini;6/24/1987;31;170.18;72.1;CF,RW,ST;Argentina;94;94;110500000;565000;LEFT");
        Player player5 = Player.of("L. Insigne;Lorenzo Insigne;6/4/1991;27;162.56;59;LW,ST;Italy;88;88;62000000;165000;RIGHT");

        Map<Position, Set<Player>> expected = new HashMap<>();
        expected.put(Position.ST, Set.of(player1, player2, player3, player4, player5));
        expected.put(Position.CM, Set.of(player2));
        expected.put(Position.RW, Set.of(player2, player4));
        expected.put(Position.CF, Set.of(player4));
        expected.put(Position.LW, Set.of(player5));
        for (Position position : Position.values()) {
            assertEquals(expected.get(position), footballPlayerAnalyzer.groupByPosition().get(position));
        }
    }
}
