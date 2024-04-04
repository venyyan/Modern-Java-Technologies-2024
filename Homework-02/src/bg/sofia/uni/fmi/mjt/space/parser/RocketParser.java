package bg.sofia.uni.fmi.mjt.space.parser;

import java.util.Optional;

public class RocketParser {
    public static Optional<String> parseWiki(String wiki) {
        String wikiWithoutQuotes = wiki.replace("\"", "");
        Optional<String> parsedWiki;
        if (wikiWithoutQuotes.isEmpty()) {
            parsedWiki = Optional.empty();
        } else {
            parsedWiki = Optional.of(wikiWithoutQuotes);
        }
        return parsedWiki;
    }

    public static Optional<Double> parseHeight(String height) {
        String heightWithoutQuotes = height.replace("\"", "");
        Optional<Double> parsedHeight;
        if (height.isEmpty()) {
            parsedHeight = Optional.empty();
        } else {
            String numericHeight = heightWithoutQuotes.replaceAll("m", "");
            parsedHeight = Optional.of(Double.parseDouble(numericHeight));
        }
        return parsedHeight;
    }
}
