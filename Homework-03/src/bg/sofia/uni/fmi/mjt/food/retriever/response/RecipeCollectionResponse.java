package bg.sofia.uni.fmi.mjt.food.retriever.response;

import bg.sofia.uni.fmi.mjt.food.recipe.RecipeCollection;

public class RecipeCollectionResponse {
    public static final int STATUS_CODE_GOOD = 200;

    private final int statusCode;
    private final ErrorResponseCollection error;
    private final RecipeCollection recipes;

    public static RecipeCollectionResponseBuilder builder(int statusCode) {
        return new RecipeCollectionResponseBuilder(statusCode);
    }

    private RecipeCollectionResponse(RecipeCollectionResponseBuilder builder) {
        this.statusCode = builder.statusCode;
        this.recipes = builder.recipes;
        this.error = builder.error;
    }

    public int getStatusCode() {
        return this.statusCode;
    }

    public ErrorResponseCollection getError() {
        return this.error;
    }

    public RecipeCollection getRecipes() {
        return this.recipes;
    }

    public boolean isErrorResponse() {
        return this.statusCode != STATUS_CODE_GOOD;
    }

    public boolean hasNextPage() {
        return this.recipes._links() != null && this.recipes._links().next() != null &&
            this.recipes._links().next().href() != null && !this.recipes._links().next().href().isEmpty();
    }

    public static class RecipeCollectionResponseBuilder {
        private final int statusCode;
        private ErrorResponseCollection error;
        private RecipeCollection recipes;

        private RecipeCollectionResponseBuilder(int statusCode) {
            this.statusCode = statusCode;
        }

        public RecipeCollectionResponseBuilder setRecipes(RecipeCollection recipes) {
            this.recipes = recipes;
            return this;
        }

        public RecipeCollectionResponseBuilder setError(ErrorResponseCollection error) {
            this.error = error;
            return this;
        }

        public RecipeCollectionResponse build() {
            return new RecipeCollectionResponse(this);
        }
    }
}
