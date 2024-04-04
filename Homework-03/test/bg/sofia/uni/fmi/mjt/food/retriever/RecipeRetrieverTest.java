package bg.sofia.uni.fmi.mjt.food.retriever;

import bg.sofia.uni.fmi.mjt.food.exception.ErrorResponseException;
import bg.sofia.uni.fmi.mjt.food.recipe.Recipe;
import bg.sofia.uni.fmi.mjt.food.recipe.RecipeCollection;
import bg.sofia.uni.fmi.mjt.food.recipe.RecipeLinks;
import bg.sofia.uni.fmi.mjt.food.recipe.RecipeWrapper;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class RecipeRetrieverTest {
    private final RecipeWrapper recipe1 = new RecipeWrapper(
        new Recipe("Basic Seitan Recipe", List.of("High-Protein", "Low-Fat", "Low-Carb"),
            List.of("Sugar-Conscious", "Low Potassium", "Kidney-Friendly", "Vegan", "Vegetarian", "Pescatarian",
                "Mediterranean", "Dairy-Free", "Egg-Free", "Peanut-Free", "Tree-Nut-Free", "Soy-Free", "Fish-Free",
                "Shellfish-Free", "Pork-Free", "Red-Meat-Free", "Crustacean-Free", "Celery-Free", "Mustard-Free",
                "Sesame-Free", "Lupine-Free", "Mollusk-Free", "Alcohol-Free", "No oil added", "Sulfite-Free", "Kosher"),
            List.of("* 1 cup vital wheat gluten", "* 1 teaspoon salt", "* 1 teaspoon garlic powder", "* 3/4 cup water"),
            454.261, 302.22969502981186, List.of("british"), List.of("breakfast"), List.of("bread")));

    private final RecipeWrapper recipe2 = new RecipeWrapper(
        new Recipe("Tilapia With Douchi (Black Fermented Beans) Recipe",
            List.of("High-Protein", "Low-Fat", "Low-Carb", "Low-Sodium"),
            List.of("Sugar-Conscious", "Keto-Friendly", "Pescatarian", "Mediterranean", "Dairy-Free", "Gluten-Free",
                "Wheat-Free", "Egg-Free", "Peanut-Free", "Tree-Nut-Free", "Soy-Free", "Shellfish-Free", "Pork-Free",
                "Red-Meat-Free", "Crustacean-Free", "Celery-Free", "Mustard-Free", "Sesame-Free", "Lupine-Free",
                "Mollusk-Free", "No oil added", "Kosher"),
            List.of("600 g tilapia fillets", "50 g ginger", "10 g black fermented beans", "30 ml millet wine",
                "70 ml water"), 674.1380903756387, 759.9254100911311, List.of("south east asian"), List.of("breakfast"),
            List.of("cereals")));

    private final RecipeWrapper recipe3 = new RecipeWrapper(
        new Recipe("Soy Sauce-Poached Halibut Cheeks", List.of("High-Protein", "Low-Fat", "Low-Carb"),
            List.of("Sugar-Conscious", "Keto-Friendly", "Pescatarian", "Dairy-Free", "Egg-Free", "Peanut-Free",
                "Tree-Nut-Free", "Shellfish-Free", "Pork-Free", "Red-Meat-Free", "Crustacean-Free", "Celery-Free",
                "Mustard-Free", "Sesame-Free", "Lupine-Free", "Mollusk-Free", "No oil added", "Kosher"),
            List.of("1 1/2 tablespoon soy sauce", "1 1/2 tablespoon mirin", "1 tablespoon lemon juice",
                "10 tablespoons water", "1 pound halibut cheeks", "1 scallion, thinly sliced"), 462.53400114389336,
            676.4239066982622, List.of("japanese"), List.of("breakfast"), List.of("starter")));

    private final RecipeWrapper recipe4 = new RecipeWrapper(
        new Recipe("Maple-Mustard Chicken Legs", List.of("Low-Carb"),
            List.of("Dairy-Free", "Gluten-Free", "Wheat-Free", "Egg-Free", "Peanut-Free", "Tree-Nut-Free", "Soy-Free",
                "Fish-Free", "Shellfish-Free", "Pork-Free", "Red-Meat-Free", "Crustacean-Free", "Celery-Free",
                "Sesame-Free", "Lupine-Free", "Mollusk-Free", "Alcohol-Free", "FODMAP-Free", "Kosher"), List.of(
            "12 oz. baby potatoes, halved", "8 oz. small carrots, halved", "2 tbsp. extra-virgin olive oil, divided",
            "Kosher salt", "Freshly ground black pepper", "4 whole chicken legs with thighs attached, room temperature",
            "2 tbsp. Dijon mustard", "2 tbsp. whole-grain mustard", "1 tbsp. maple syrup", "1 tsp. fresh thyme leaves",
            "Pinch crushed red pepper flakes", "Freshly chopped parsley, for garnish"),
            3651.4209998331175, 2087.0035547061416, List.of("american"), List.of("lunch/dinner"),
            List.of("main course")));

    private final RecipeWrapper recipe5 = new RecipeWrapper(
        new Recipe("Carrot Ribbons in Rosemary Butter Sauce recipes", List.of("Low-Sodium"),
            List.of("Sugar-Conscious", "Kidney-Friendly", "Keto-Friendly", "Vegetarian", "Pescatarian", "Gluten-Free",
                "Wheat-Free", "Egg-Free", "Peanut-Free", "Tree-Nut-Free", "Soy-Free", "Fish-Free", "Shellfish-Free",
                "Pork-Free", "Red-Meat-Free", "Crustacean-Free", "Celery-Free", "Mustard-Free", "Sesame-Free",
                "Lupine-Free", "Mollusk-Free", "Alcohol-Free", "Sulfite-Free", "FODMAP-Free", "Kosher"), List.of(
            "3-4 large, straight carrots (to make about 2 cups of carrot ribbons)", "1 Tbsp ghee or butter",
            "1/2 tsp fresh rosemary, minced (or ¼ tsp dried rosemary, crushed)",
            "1/2 tsp fresh parsley, minced (or ¼ tsp dried parsley flakes)", "1/4 tsp salt"),
            200.34650000001142, 228.25569965724833, List.of("mediterranean"), List.of("snack"),
            List.of("condiments and sauces")));

    @Test
    void testGetRecipeWithOnePage() throws URISyntaxException, ErrorResponseException {
        Collection<RecipeWrapper> expectedRecipes = List.of(recipe1, recipe2, recipe3);
        RecipeCollection expectedRecipeCollection = new RecipeCollection(new RecipeLinks(null), expectedRecipes);
        RecipeCollection actual = RecipeRetriever.getRecipes(new URI(
                "https://api.edamam.com/api/recipes/v2?" +
                    "type=public&app_id=0c6e4aaf&app_key=3aa895c1353d1bd59402e94c71e01310&diet=high-protein&diet=low-carb&diet=low-fat&health=celery-free&health=crustacean-free&health=dairy-free&health=egg-free&mealType=Breakfast"),
            1);
        assertEquals(expectedRecipeCollection, actual, "Incorrect recipe collection for this request. Expected: " +
            expectedRecipeCollection + ", but was: " +
            actual);
    }

    @Test
    void testGetRecipeWithInvalidURI() throws URISyntaxException, ErrorResponseException {
        assertThrows(ErrorResponseException.class, () -> RecipeRetriever.getRecipes(new URI(
            "https://api.edamam.com/api/recipes/v2?" +
                "type=public&app_id=0c6e4aaf&app_key=3aa895c1353d1bd59402e94c71e01310&" +
                "diet=high-protein&diet=low-carb&diet=low-fat&health=celery-fre&" +
                "health=crustacean-free&health=dairy-free&health=egg-free&mealType=Breakfast"), 1),
            "ErrorResponseException expected to be thrown!");
    }

    @Test
    void testGetRecipeWithMultiplePagesBySize() throws URISyntaxException, ErrorResponseException {
        int actualSize = RecipeRetriever.getRecipes(new URI(
                "https://api.edamam.com/api/recipes/v2?" + "type=public&q=chicken%2C%20egg&app_id=0c6e4aaf&" +
                    "app_key=3aa895c1353d1bd59402e94c71e01310&health=crustacean-free&mealType=Dinner&mealType=Lunch"), 2)
            .hits().size();
        assertEquals(40, actualSize, "Incorrect count of recipes returned. Expected 40, but was: " + actualSize);
    }

    @Test
    void testGetRecipeWithMultiplePages() throws URISyntaxException, ErrorResponseException {
        RecipeWrapper expected = recipe4;
        RecipeWrapper actual = (RecipeWrapper) RecipeRetriever.getRecipes(new URI("https://api.edamam.com/api/recipes/v2?" +
            "type=public&q=parsley%2C%20carrot&app_id=0c6e4aaf&app_key=3aa895c1353d1bd59402e94c71e01310&health=egg-free&" +
            "health=fodmap-free&health=gluten-free&health=wheat-free"), 3).hits().toArray()[40];
        assertEquals(recipe4, actual, "Incorrect recipe from page 3! Expected: " + expected + ", but was: "
            + actual);
    }
}
