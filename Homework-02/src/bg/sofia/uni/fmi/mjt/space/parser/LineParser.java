package bg.sofia.uni.fmi.mjt.space.parser;

public class LineParser {
    private static final int MISSION_TOKENS_COUNT = 8;
    private static final char MISSION_ATTRIBUTE_DELIMITER = ',';

    public static String[] parse(String line) {
        String[] tokens = new String[MISSION_TOKENS_COUNT];

        boolean isInQuote = false;
        int tokenId = 0;
        int quoteId = 0;
        StringBuilder token = new StringBuilder();

        for (char ch : line.toCharArray()) {
            if (ch == '\"' && quoteId == 0) {
                isInQuote = true;
                quoteId = 1;
            } else if (ch == '\"') {
                isInQuote = false;
                quoteId = 0;
            }
            if (ch == MISSION_ATTRIBUTE_DELIMITER && !isInQuote) {
                tokens[tokenId++] = token.toString().replace("\"", "").trim();
                token.delete(0, token.length());
            } else {
                token.append(ch);
            }
        }

        tokens[tokenId] = token.toString().replace("\"", "").trim();
        token.delete(0, token.length());
        return tokens;
    }
}
