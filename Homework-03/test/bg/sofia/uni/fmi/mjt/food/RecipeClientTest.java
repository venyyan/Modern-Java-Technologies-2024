package bg.sofia.uni.fmi.mjt.food;

import bg.sofia.uni.fmi.mjt.food.exception.ErrorResponseException;
import bg.sofia.uni.fmi.mjt.food.recipe.Recipe;
import bg.sofia.uni.fmi.mjt.food.retriever.request.parameters.Health;
import bg.sofia.uni.fmi.mjt.food.retriever.request.parameters.MealType;
import org.junit.jupiter.api.Test;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertIterableEquals;

public class RecipeClientTest {
    private final Recipe recipe1 = new Recipe("Heart Healthy cooking with Green Tea recipes",
        List.of("Low-Sodium"),
        List.of("Sugar-Conscious", "Pescatarian", "Dairy-Free", "Gluten-Free", "Wheat-Free", "Egg-Free", "Peanut-Free",
            "Tree-Nut-Free", "Soy-Free", "Shellfish-Free", "Pork-Free", "Red-Meat-Free", "Crustacean-Free",
            "Celery-Free", "Mustard-Free", "Sesame-Free", "Lupine-Free", "Mollusk-Free", "Alcohol-Free", "No oil added",
            "Sulfite-Free"),
        List.of("3.5 oz (100 gram) cooked Salmon", "1 cup green tea (freshly brewed)", "1/2 cup cooked white rice",
            "2 oz raw (50 grams) Scallions/Spring Onions", "5 raw Sprigs Parsley"),
        349.67822315, 498.9223771875, List.of("mediterranean"), List.of("teatime"),
        List.of("condiments and sauces"));

    private final Recipe recipe2 = new Recipe("Vegetarian recipes",
            List.of("High-Fiber", "Low-Fat"),
            List.of("Sugar-Conscious", "Dairy-Free", "Egg-Free", "Peanut-Free", "Tree-Nut-Free", "Fish-Free",
                "Shellfish-Free", "Pork-Free", "Red-Meat-Free", "Crustacean-Free", "Celery-Free", "Mustard-Free",
                "Sesame-Free", "Lupine-Free", "Mollusk-Free", "Alcohol-Free", "Kosher"),
            List.of("1 onion", "½ cup oatmeal", "½ cup wheat germ", "2 T brewer's yeast flakes",
                "2 T chicken seasoning", "1½ c water", "1½ c gluten flour (dough pep)", "10 cup water",
                "½ cup soy sauce", "2 T chicken seasoning", "1 pkg Lipton onion soup mix",
                "1 cup flour or bread crumbs", "2 Tbsp nutrition yeast flakes", "1 Tbsp dry parsley flakes",
                "½ tsp onion powder", "½ tsp garlic powder", "¼ tsp paprika"),
            1987.542, 3482.6897197499998, List.of("american"), List.of("teatime"),
            List.of("starter"));

    private final Recipe recipe3 = new Recipe("* 4 tbsp fresh lime juice",
            List.of("Low-Fat"),
            List.of("Vegan", "Vegetarian", "Pescatarian", "Dairy-Free", "Egg-Free", "Peanut-Free", "Tree-Nut-Free",
                "Fish-Free", "Shellfish-Free", "Pork-Free", "Red-Meat-Free", "Crustacean-Free", "Celery-Free",
                "Sesame-Free", "Lupine-Free", "Mollusk-Free", "Alcohol-Free", "No oil added", "Kosher"),
            List.of("* 2 clove garlic, smashed", "* 1 1/2 tbsp maple syrup", "* 1 tbsp reduced salt soy sauce",
                "* 1 tbsp white miso paste", "* 1 tsp wholegrain mustard", "salad:",
                "* 600g (c 21 oz) jersey royal potato", "* 275g (c 10 oz) canned sweetcorn", "* 1 large red pepper",
                "* 1 jalapeno", "* 1 large onion", "* 2 tbsp chopped parsley", "* 100g black olive"),
            1056.016, 1384.6, List.of("french"), List.of("teatime"),
            List.of("starter"));

    private final Recipe recipe4 = new Recipe("CSA Tuesday recipes",
            List.of("High-Fiber"),
            List.of("Dairy-Free", "Gluten-Free", "Wheat-Free", "Egg-Free", "Peanut-Free", "Tree-Nut-Free", "Soy-Free",
                "Fish-Free", "Shellfish-Free", "Crustacean-Free", "Celery-Free", "Mustard-Free", "Sesame-Free",
                "Lupine-Free", "Mollusk-Free", "No oil added"),
            List.of("1 lb. ground beef", "1 lb. chorizo", "2 lb. San Marzano tomatoes, cut into fourths",
                "28 oz. crushed tomatoes", "1 lb. bell peppers, stems and seeds removed then chopped",
                "2 onions, chopped", "5 garlic cloves, minced", "½ c. red wine", "1 t. black pepper", "1 t. sea salt",
                "1 can (6 oz.) tomato paste", "2 T. fresh parsley, chopped", "2 T. fresh oregano, chopped",
                "2 T. basil, chopped"),
            3405.4956853750577, 3627.0922567092293, List.of("american"), List.of("teatime"),
            List.of("drinks"));

    @Test
    void tesGetAllRecipesWithParameters() throws ErrorResponseException, URISyntaxException {
        Collection<Recipe> expectedRecipes = List.of(recipe1, recipe2, recipe3, recipe4);
        RecipeClient client = new RecipeClient();

        assertIterableEquals(expectedRecipes, client.getRecipeByKeywords(List.of("parsley", "onion"))
            .getRecipeByMealType(EnumSet.of(MealType.TEATIME))
            .getRecipeByHealth(
                EnumSet.of(Health.CELERY_FREE, Health.CRUSTACEAN_FREE, Health.DAIRY_FREE, Health.EGG_FREE)).getAll());
    }

    @Test
    void testGetAllRecipesWithNoParameters() throws ErrorResponseException, URISyntaxException {
        Collection<Recipe> expectedRecipes = new ArrayList<>();
        RecipeClient client = new RecipeClient();

        assertIterableEquals(expectedRecipes, client.getAll());
    }
}
