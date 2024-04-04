package bg.sofia.uni.fmi.mjt.food.retriever.request;

import org.junit.jupiter.api.Test;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RecipeRequestTest {

    @Test
    void testGenerateURIWithKeywords() throws URISyntaxException {
        Collection<String> keywords = List.of("egg", "chicken");
        RecipeRequest requestWithKeywords = new RecipeRequest();
        requestWithKeywords.setKeywords(keywords);

        URI expectedURI = new URI("https://api.edamam.com/api/recipes/v2?" +
            "type=public&q=egg%2C%20chicken&app_id=0c6e4aaf&app_key=3aa895c1353d1bd59402e94c71e01310");
        assertEquals(expectedURI, requestWithKeywords.generateURI(),
            "Incorrect URI for this request. Expected: " + expectedURI + ", but was: " +
                requestWithKeywords.generateURI());
    }

    @Test
    void testGenerateURIWithMealType() throws URISyntaxException {
        Collection<String> mealType = List.of("Breakfast", "Dinner");
        RecipeRequest requestWithKeywords = new RecipeRequest();
        requestWithKeywords.setMealType(mealType);

        URI expectedURI = new URI("https://api.edamam.com/api/recipes/v2?" +
            "type=public&app_id=0c6e4aaf&app_key=3aa895c1353d1bd59402e94c71e01310&mealType=Breakfast&mealType=Dinner");
        assertEquals(expectedURI, requestWithKeywords.generateURI(),
            "Incorrect URI for this request. Expected: " + expectedURI + ", but was: " +
                requestWithKeywords.generateURI());
    }

    @Test
    void testGenerateURIWithHealth() throws URISyntaxException {
        Collection<String> health = List.of("dairy-free", "egg-free", "fish-free");
        RecipeRequest requestWithKeywords = new RecipeRequest();
        requestWithKeywords.setHealth(health);

        URI expectedURI = new URI("https://api.edamam.com/api/recipes/v2?" +
            "type=public&app_id=0c6e4aaf&app_key=3aa895c1353d1bd59402e94c71e01310&health=dairy-free&health=egg-free&health=fish-free");
        assertEquals(expectedURI, requestWithKeywords.generateURI(),
            "Incorrect URI for this request. Expected: " + expectedURI + ", but was: " +
                requestWithKeywords.generateURI());
    }

    @Test
    void testGenerateURIWithKeywordsAndMealType() throws URISyntaxException {
        Collection<String> mealType = List.of("Breakfast", "Teatime");
        Collection<String> keywords = List.of("onion", "chicken");

        RecipeRequest requestWithKeywordsMealType = new RecipeRequest();
        requestWithKeywordsMealType.setMealType(mealType);
        requestWithKeywordsMealType.setKeywords(keywords);

        URI expectedURI = new URI(
            "https://api.edamam.com/api/recipes/v2?type=public&" +
                "q=onion%2C%20chicken&app_id=0c6e4aaf&app_key=3aa895c1353d1bd59402e94c71e01310&mealType=Breakfast&mealType=Teatime");
        assertEquals(expectedURI, requestWithKeywordsMealType.generateURI(),
            "Incorrect URI for this request. Expected: " + expectedURI + ", but was: " +
                requestWithKeywordsMealType.generateURI());
    }

    @Test
    void testGenerateURIWithKeywordsAndHealth() throws URISyntaxException {
        Collection<String> health = List.of("sesame-free", "sulfite-free");
        Collection<String> keywords = List.of("pork", "egg");

        RecipeRequest requestWithKeywordsHealth = new RecipeRequest();
        requestWithKeywordsHealth.setHealth(health);
        requestWithKeywordsHealth.setKeywords(keywords);

        URI expectedURI = new URI(
            "https://api.edamam.com/api/recipes/v2?type=public&q=pork%2C%20egg&app_id=0c6e4aaf&app_key=3aa895c1353d1bd59402e94c71e01310&health=sesame-free&health=sulfite-free");
        assertEquals(expectedURI, requestWithKeywordsHealth.generateURI(),
            "Incorrect URI for this request. Expected: " + expectedURI + ", but was: " +
                requestWithKeywordsHealth.generateURI());
    }

    @Test
    void testGenerateURIWithMealTypeAndHealth() throws URISyntaxException {
        Collection<String> health = List.of("sesame-free", "sulfite-free");
        Collection<String> mealType = List.of("Breakfast", "Teatime");

        RecipeRequest requestWithMealTypeHealth = new RecipeRequest();
        requestWithMealTypeHealth.setHealth(health);
        requestWithMealTypeHealth.setMealType(mealType);

        URI expectedURI = new URI(
            "https://api.edamam.com/api/recipes/v2?type=public&" +
                "app_id=0c6e4aaf&app_key=3aa895c1353d1bd59402e94c71e01310&health=sesame-free&health=sulfite-free&mealType=Breakfast&mealType=Teatime");
        assertEquals(expectedURI, requestWithMealTypeHealth.generateURI(),
            "Incorrect URI for this request. Expected: " + expectedURI + ", but was: " +
                requestWithMealTypeHealth.generateURI());
    }

    @Test
    void testGenerateURIWithMealTypeAndHealthAndKeywords() throws URISyntaxException {
        Collection<String> health = List.of("sesame-free", "sulfite-free");
        Collection<String> mealType = List.of("Breakfast", "Teatime");
        Collection<String> keywords = List.of("pork", "egg");

        RecipeRequest requestWithMealTypeHealthKeywords = new RecipeRequest();
        requestWithMealTypeHealthKeywords.setHealth(health);
        requestWithMealTypeHealthKeywords.setMealType(mealType);
        requestWithMealTypeHealthKeywords.setKeywords(keywords);

        URI expectedURI = new URI(
            "https://api.edamam.com/api/recipes/v2?type=public&q=pork%2C%20egg&app_id=0c6e4aaf&app_key=3aa895c1353d1bd59402e94c71e01310&health=sesame-free&health=sulfite-free&mealType=Breakfast&mealType=Teatime");
        assertEquals(expectedURI, requestWithMealTypeHealthKeywords.generateURI(),
            "Incorrect URI for this request. Expected: " + expectedURI + ", but was: " +
                requestWithMealTypeHealthKeywords.generateURI());
    }

    @Test
    void testGenerateURIWithNoCategories() throws URISyntaxException {
        RecipeRequest requestWithNothing = new RecipeRequest();

        URI expectedURI = new URI(
            "https://api.edamam.com/api/recipes/v2?type=public&app_id=0c6e4aaf&app_key=3aa895c1353d1bd59402e94c71e01310");
        assertEquals(expectedURI, requestWithNothing.generateURI(),
            "Incorrect URI for this request. Expected: " + expectedURI + ", but was: " +
                requestWithNothing.generateURI());
    }
}
