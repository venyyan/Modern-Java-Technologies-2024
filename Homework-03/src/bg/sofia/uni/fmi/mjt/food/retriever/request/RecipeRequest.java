package bg.sofia.uni.fmi.mjt.food.retriever.request;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;

public class RecipeRequest {
    private static final String API_HOST = "https://api.edamam.com";
    private static final String API_PATH = "/api/recipes/v2?";
    private static final String API_TYPE = "type=public";

    private static final String API_AND = "&";
    private static final String API_KEYWORDS_AND = "%2C%20";

    private static final String API_ID = "app_id=";
    private static final String API_KEY = "app_key=";
    private static final String API_KEYWORDS_IDENTIFIER = "q=";
    private static final String API_MEAL_TYPE_IDENTIFIER = "mealType=";
    private static final String API_HEALTH_TYPE_IDENTIFIER = "health=";

    public static final String API_PERSONAL_ID = "0c6e4aaf";
    public static final String API_PERSONAL_KEY = "3aa895c1353d1bd59402e94c71e01310";

    private Collection<String> keywords;
    private Collection<String> health;
    private Collection<String> mealType;

    public void setKeywords(Collection<String> keywords) {
        this.keywords = keywords;
    }

    public void setHealth(Collection<String> health) {
        this.health = health;
    }

    public void setMealType(Collection<String> mealType) {
        this.mealType = mealType;
    }

    public URI generateURI() throws URISyntaxException {
        StringBuilder uriBuilder = new StringBuilder();

        uriBuilder.append(API_HOST).append(API_PATH).append(API_TYPE);
        if (keywords != null) {
            uriBuilder.append(API_AND).append(API_KEYWORDS_IDENTIFIER)
                .append(String.join(API_KEYWORDS_AND, keywords));
        }
        uriBuilder.append(API_AND).append(API_ID).append(API_PERSONAL_ID)
            .append(API_AND).append(API_KEY).append(API_PERSONAL_KEY);

        if (health != null) {
            appendIdentifier(uriBuilder, API_HEALTH_TYPE_IDENTIFIER, health);
        }

        if (mealType != null) {
            appendIdentifier(uriBuilder, API_MEAL_TYPE_IDENTIFIER, mealType);
        }
        return new URI(uriBuilder.toString());
    }

    private void appendIdentifier(StringBuilder currentUri, String idType,
                                  Collection<String> identifiers) {
        currentUri.append(API_AND).append(idType)
            .append(String.join(RecipeRequest.API_AND + idType, identifiers));
    }
}
