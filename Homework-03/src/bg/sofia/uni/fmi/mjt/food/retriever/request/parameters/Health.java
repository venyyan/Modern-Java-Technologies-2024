package bg.sofia.uni.fmi.mjt.food.retriever.request.parameters;

public enum Health {
    ALCOHOL_COCKTAIL("alcohol-cocktail"),
    ALCOHOL_FREE("alcohol-free"),
    CELERY_FREE("celery-free"),
    CRUSTACEAN_FREE("crustacean-free"),
    DAIRY_FREE("dairy-free"),
    DASH("DASH"),
    EGG_FREE("egg-free"),
    FISH_FREE("fish-free"),
    FODMAP_FREE("fodmap-free"),
    GLUTEN_FREE("gluten-free"),
    IMMUNO_SUPPORTIVE("immuno-supportive"),
    KETO_FRIENDLY("keto-friendly"),
    KIDNEY_FRIENDLY("kidney-friendly"),
    KOSHER("kosher"),
    LOW_FAT_ABS("low-fat-abs"),
    LOW_POTASSIUM("low-potassium"),
    LOW_SUGAR("low-sugar"),
    LUPINE_FREE("lupine-free"),
    MEDITERRANEAN("mediterranean"),
    MOLLUSK_FREE("mollusk-free"),
    MUSTARD_FREE("mustard-free"),
    NO_OIL_ADDED("no-oil-added"),
    PALEO("paleo"),
    PEANUT_FREE("peanut-free"),
    PESCATARIAN("pescatarian"),
    PORK_FREE("pork-free"),
    RED_MEAT_FREE("red-meat-free"),
    SESAME_FREE("sesame-free"),
    SHELLFISH_FREE("shellfish-free"),
    SOY_FREE("soy-free"),
    SUGAR_CONSCIOUS("sugar_conscious"),
    SULFITE_FREE("sulfite-free"),
    TREE_NUT_FREE("tree-nut-free"),
    VEGAN("vegan"),
    VEGETARIAN("vegetarian"),
    WHEAT_FREE("wheat-free");

    final String name;

    Health(String name) {
        this.name = name;
    }

    public String health() {
        return this.name;
    }
}
