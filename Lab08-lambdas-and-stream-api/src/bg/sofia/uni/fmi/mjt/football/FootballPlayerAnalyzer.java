package bg.sofia.uni.fmi.mjt.football;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class FootballPlayerAnalyzer {
    private final List<Player> players;
    private static final int MAX_RATING_DIFF = 3;

    public FootballPlayerAnalyzer(Reader reader) {
        try (var buffReader = new BufferedReader(reader)) {
            players = buffReader.lines().skip(1).map(Player::of).toList();
        } catch (IOException e) {
            throw new UncheckedIOException("A problem occurred while trying to read from file!", e);
        }
    }

    public List<Player> getAllPlayers() {
        return Collections.unmodifiableList(List.copyOf(players));
    }

    public Set<String> getAllNationalities() {
        return players.stream()
            .map(Player::nationality)
            .collect(Collectors.toSet());
    }

    public Player getHighestPaidPlayerByNationality(String nationality) {
        if (nationality == null || nationality.isEmpty()) {
            throw new IllegalArgumentException("Nationality is null or empty!");
        }
        return players.stream()
            .filter(p -> p.nationality().equals(nationality))
            .max(Comparator.comparing(Player::wageEuro))
            .orElseThrow(() -> new NoSuchElementException("No such player found!"));
    }

    public Map<Position, Set<Player>> groupByPosition() {
        Map<Position, Set<Player>> result = new HashMap<>();

        for (Position position : Position.values()) {
            Set<Player> playersForPosition = players.stream()
                .filter(player -> player.positions().contains(position))
                .collect(Collectors.toSet());

            if (!playersForPosition.isEmpty()) {
                result.put(position, playersForPosition);
            }
        }

        return result;
    }

    public Optional<Player> getTopProspectPlayerForPositionInBudget(Position position, long budget) {
        if (position == null) {
            throw new IllegalArgumentException("Position is null!");
        }

        if (budget < 0) {
            throw new IllegalArgumentException("Budget cannot be negative!");
        }

        return players.stream()
            .filter(p -> p.positions().contains(position))
            .filter(p -> p.valueEuro() <= budget)
            .max(Comparator.comparingDouble(p -> (double) (p.overallRating() + p.potential()) / p.age()));
    }

    public Set<Player> getSimilarPlayers(Player player) {
        if (player == null) {
            throw new IllegalArgumentException("Player is null!");
        }

        Set<Player> res = players.stream()
            .filter(p -> p.preferredFoot().equals(player.preferredFoot()))
            .filter(p -> p.overallRating() <= player.overallRating() + MAX_RATING_DIFF &&
                p.overallRating() + MAX_RATING_DIFF >= player.overallRating())
            .filter(p -> p.positions().stream().anyMatch(player.positions()::contains))
            .collect(Collectors.toSet());

        return Set.copyOf(res);
    }

    public Set<Player> getPlayersByFullNameKeyword(String keyword) {
        if (keyword == null) {
            throw new IllegalArgumentException("Keyword is null!");
        }

        Set<Player> res =  players.stream()
            .filter(p -> p.fullName().contains(keyword))
            .collect(Collectors.toSet());

        return Set.copyOf(res);
    }
}
