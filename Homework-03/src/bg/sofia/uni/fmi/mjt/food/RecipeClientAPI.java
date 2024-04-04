package bg.sofia.uni.fmi.mjt.food;

import bg.sofia.uni.fmi.mjt.food.exception.ErrorResponseException;
import bg.sofia.uni.fmi.mjt.food.recipe.Recipe;
import bg.sofia.uni.fmi.mjt.food.retriever.request.parameters.Health;
import bg.sofia.uni.fmi.mjt.food.retriever.request.parameters.MealType;

import java.net.URISyntaxException;
import java.util.Collection;
import java.util.EnumSet;

public interface RecipeClientAPI {

    RecipeClient getRecipeByKeywords(Collection<String> keywordsParam);

    RecipeClient getRecipeByHealth(EnumSet<Health> healthParams);

    RecipeClient getRecipeByMealType(EnumSet<MealType> mealTypeParam);

    Collection<Recipe> getAll() throws URISyntaxException, ErrorResponseException;
}
