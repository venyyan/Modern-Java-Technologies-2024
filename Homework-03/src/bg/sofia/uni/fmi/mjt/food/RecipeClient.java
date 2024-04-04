package bg.sofia.uni.fmi.mjt.food;

import bg.sofia.uni.fmi.mjt.food.exception.ErrorResponseException;
import bg.sofia.uni.fmi.mjt.food.recipe.Recipe;
import bg.sofia.uni.fmi.mjt.food.recipe.RecipeWrapper;
import bg.sofia.uni.fmi.mjt.food.retriever.RecipeRetriever;
import bg.sofia.uni.fmi.mjt.food.retriever.request.RecipeRequest;
import bg.sofia.uni.fmi.mjt.food.retriever.request.parameters.Health;
import bg.sofia.uni.fmi.mjt.food.retriever.request.parameters.MealType;

import java.net.URISyntaxException;
import java.util.Collection;
import java.util.EnumSet;
import java.util.stream.Collectors;

public class RecipeClient implements RecipeClientAPI {
    private static final int PAGES_TO_RETURN = 3;

    private final RecipeRequest requestBuilder;
    public RecipeClient() {
        requestBuilder = new RecipeRequest();
    }

    public RecipeClient getRecipeByKeywords(Collection<String> keywordsParam) {
        requestBuilder.setKeywords(keywordsParam);
        return this;
    }

    public RecipeClient getRecipeByHealth(EnumSet<Health> healthParams) {
        Collection<String> healthStr = healthParams.stream()
            .map(Health::health)
            .toList();
        requestBuilder.setHealth(healthStr);
        return this;
    }

    public RecipeClient getRecipeByMealType(EnumSet<MealType> mealTypeParam) {
        Collection<String> mealTypeStr = mealTypeParam.stream()
            .map(MealType::mealType)
            .toList();
        this.requestBuilder.setMealType(mealTypeStr);
        return this;
    }

    public Collection<Recipe> getAll() throws URISyntaxException, ErrorResponseException {
        return RecipeRetriever.getRecipes(this.requestBuilder.generateURI(), PAGES_TO_RETURN)
            .hits().stream().map(RecipeWrapper::recipe).collect(Collectors.toList());
    }
}
