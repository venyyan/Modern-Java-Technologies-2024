package bg.sofia.uni.fmi.mjt.space.rocket;

import bg.sofia.uni.fmi.mjt.space.parser.RocketParser;

import java.util.Arrays;
import java.util.Optional;

import static bg.sofia.uni.fmi.mjt.space.parser.LineParser.parse;

public record Rocket(String id, String name, Optional<String> wiki, Optional<Double> height) {
    private static final int ID_TOKEN = 0;
    private static final int NAME_TOKEN = 1;
    private static final int WIKI_TOKEN = 2;
    private static final int HEIGHT_TOKEN = 3;
    private static final int ROCKET_TOKENS_COUNT = 4;

    public static Rocket of(String line) {
        String[] tokens = parse(line);
        addEmptyTokensIfWikiHeightIsEmpty(tokens);

        String id = tokens[ID_TOKEN];
        String name = tokens[NAME_TOKEN];
        Optional<String> wiki = RocketParser.parseWiki(tokens[WIKI_TOKEN]);
        Optional<Double> height = RocketParser.parseHeight(tokens[HEIGHT_TOKEN]);

        return new Rocket(id, name, wiki, height);
    }

    private static void addEmptyTokensIfWikiHeightIsEmpty(String[] tokens) {
        while (tokens.length < ROCKET_TOKENS_COUNT) {
            tokens = Arrays.copyOf(tokens, tokens.length + 1);
        }

        for (int i = WIKI_TOKEN; i < ROCKET_TOKENS_COUNT; i++) {
            if (tokens[i] == null) {
                tokens[i] = "";
            }
        }
    }
}