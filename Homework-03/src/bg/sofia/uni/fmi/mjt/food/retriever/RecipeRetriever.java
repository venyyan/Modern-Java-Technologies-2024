package bg.sofia.uni.fmi.mjt.food.retriever;

import bg.sofia.uni.fmi.mjt.food.exception.ErrorResponseException;
import bg.sofia.uni.fmi.mjt.food.recipe.RecipeCollection;
import bg.sofia.uni.fmi.mjt.food.retriever.response.ErrorResponseCollection;
import bg.sofia.uni.fmi.mjt.food.retriever.response.RecipeCollectionResponse;
import bg.sofia.uni.fmi.mjt.food.retriever.request.RecipeRequest;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;

import com.google.gson.Gson;

import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RecipeRetriever {
    private static final int ERROR_MESSAGE_ID = 0;

    private RecipeRetriever() {
    }

    private static RecipeCollectionResponse getHttpRecipesResponse(URI uri) throws ErrorResponseException {
        try (ExecutorService executor = Executors.newCachedThreadPool()) {
            HttpClient client =
                HttpClient.newBuilder().executor(executor).build();

            HttpRequest httpRequest = HttpRequest.newBuilder().uri(uri)
                .setHeader(RecipeRequest.API_PERSONAL_ID, RecipeRequest.API_PERSONAL_KEY).build();

            CompletableFuture<RecipeCollectionResponse> future = getFuture(client, httpRequest);

            RecipeCollectionResponse response = future.join();

            if (response.isErrorResponse()) {
                throw new ErrorResponseException(response.getStatusCode(),
                    response.getError().errors().get(ERROR_MESSAGE_ID).error());
            }

            return response;
        }
    }

    private static CompletableFuture<RecipeCollectionResponse> getFuture(HttpClient client, HttpRequest httpRequest) {
        Gson gson = new Gson();
        return client.sendAsync(httpRequest, HttpResponse.BodyHandlers.ofString())
            .thenApply(httpResponse -> {
                RecipeCollectionResponse response;
                if (httpResponse.statusCode() != RecipeCollectionResponse.STATUS_CODE_GOOD) {
                    response = RecipeCollectionResponse.builder(httpResponse.statusCode())
                        .setError(gson.fromJson(httpResponse.body(), ErrorResponseCollection.class)).build();
                } else {
                    response = RecipeCollectionResponse.builder(httpResponse.statusCode())
                        .setRecipes(gson.fromJson(httpResponse.body(), RecipeCollection.class)).build();
                }
                return response;
            });
    }

    public static RecipeCollection getRecipes(URI uri, int desiredPages)
        throws ErrorResponseException, URISyntaxException {

        RecipeCollectionResponse firstPageResponse = getHttpRecipesResponse(uri);
        RecipeCollectionResponse nextPageResponse = firstPageResponse;

        int pagesCounter = 1;
        while (pagesCounter < desiredPages) {
            if (nextPageResponse.hasNextPage()) {
                RecipeCollectionResponse nextPageRecipes =
                    getHttpRecipesResponse(new URI(nextPageResponse.getRecipes()._links().next().href()));

                firstPageResponse.getRecipes().hits().addAll(nextPageRecipes.getRecipes().hits());
                nextPageResponse = nextPageRecipes;
            } else {
                break;
            }
            pagesCounter++;
        }
        return firstPageResponse.getRecipes();
    }
}

