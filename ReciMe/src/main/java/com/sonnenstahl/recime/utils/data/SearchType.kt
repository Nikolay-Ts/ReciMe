package com.sonnenstahl.recime.utils.data

/**
 * enum so that [com.sonnenstahl.recime.Recipes] can know what type of API request to commit
 */
enum class SearchType {
    /**
     * indicates that the recipe search should be based on a specific category.
     * For example, fetching meals belonging to "Seafood" or "Dessert" categories.
     */
    CATEGORY,

    /**
     * indicates that the recipe search should be based on a list of available ingredients.
     * For example, finding recipes that can be made with "chicken" and "broccoli".
     */
    INGREDIENTS,

    /**
     * indicates that no specific search criteria are provided, typically leading to
     * a request for a random meal or a default list of recipes.
     */
    NONE,
}
