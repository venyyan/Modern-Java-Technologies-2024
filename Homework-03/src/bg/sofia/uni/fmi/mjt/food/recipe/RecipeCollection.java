package bg.sofia.uni.fmi.mjt.food.recipe;

import java.util.Collection;

public record RecipeCollection(RecipeLinks _links, Collection<RecipeWrapper> hits) {
}
