package bg.sofia.uni.fmi.mjt.food.retriever.request.parameters;

public enum MealType {
    BREAKFAST("Breakfast"),
    DINNER("Dinner"),
    LUNCH("Lunch"),
    SNACK("Snack"),
    TEATIME("Teatime");

    final String name;

    MealType(String name) {
        this.name = name;
    }

    public String mealType() {
        return this.name;
    }
}
