package bg.sofia.uni.fmi.mjt.food.recipe;

import java.util.Collection;

public record Recipe(String label, Collection<String> dietLabels, Collection<String> healthLabels,
                     Collection<String> ingredientLines, double calories, double totalWeight,
                     Collection<String> cuisineType, Collection<String> mealType,
                     Collection<String> dishType) {
}
