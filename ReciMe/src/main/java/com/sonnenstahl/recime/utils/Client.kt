package com.sonnenstahl.recime.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.sonnenstahl.recime.BuildConfig
import com.sonnenstahl.recime.utils.data.Meal
import com.sonnenstahl.recime.utils.data.MealResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.engine.okhttp.*
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.URLBuilder
import io.ktor.http.appendPathSegments
import io.ktor.http.takeFrom
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

/**
 * a singleton object to handle all of the http requests to the API
 */
object Client {
    private const val SPOON = BuildConfig.SPOON
    private val client =
        HttpClient(CIO) {
            install(ContentNegotiation) {
                json(
                    Json {
                        ignoreUnknownKeys = true
                        prettyPrint = true
                        isLenient = true
                    },
                )
            }
        }

    private val imageClient = HttpClient(OkHttp)

    /**
     * a function to fetch the image form the API
     *
     * @param mealName the name of the meal that is stored in the api
     * @param imageSize how large you want the image. Good for lowering bandwithd
     *
     * @return the image bitmap or null if it was unsuccessful
     */
    suspend fun getImage(
        mealName: String,
        imageSize: ImageSize = ImageSize.SMALL,
    ): Bitmap? {
        val imageSize =
            when (imageSize) {
                ImageSize.SMALL -> "small"
                ImageSize.MEDIUM -> "medium"
                ImageSize.LARGE -> "Large"
            }

        return try {
            val url =
                URLBuilder()
                    .apply {
                        takeFrom(mealName)
                        appendPathSegments(imageSize)
                    }.toString()

            val response = imageClient.get(url)

            if (response.status.value == 200) {
                val imageBytes = response.body<ByteArray>()
                BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
            } else {
                null
            }
        } catch (e: Exception) {
            Log.d("GET Image Error", e.toString())
            null
        }
    }

    /**
     * obtain the image data given the meal name. This does not return the image bitmap and
     * must be done with using [getImage]
     *
     * @param mealName name of the meal
     *
     * @return the [Meal] or null if it was unsuccessful
     */
    suspend fun getMealByName(mealName: String): Meal? =
        try {
            val url =
                URLBuilder()
                    .apply {
                        takeFrom(SPOON)
                        appendPathSegments("search.php")
                        parameters.append("s", mealName)
                    }.toString()

            val response = client.get(url)

            if (response.status.value == 200) {
                val rawJson = response.bodyAsText()
                Log.d("Client", "Raw JSON Response: $rawJson")

                Json.decodeFromString<MealResponse>(rawJson).meals?.firstOrNull()
            } else {
                null
            }
        } catch (e: Exception) {
            Log.d("Could not GET:", e.toString())
            null
        }

    /**
     * gets a random meal
     *
     * @return a [Meal] or null if it was unsuccessful
     */
    suspend fun getRandomMeal(): Meal? =
        try {
            val url =
                URLBuilder()
                    .apply {
                        takeFrom(SPOON)
                        appendPathSegments("random.php")
                    }.toString()

            Log.d("CRASHING_MEOW", url)
            val response = client.get(url)

            if (response.status.value == 200) {
                val rawJson = response.bodyAsText()
                Log.d("Client", "Raw JSON Response: $rawJson")

                Json.decodeFromString<MealResponse>(rawJson).meals?.firstOrNull()
            } else {
                null
            }
        } catch (e: Exception) {
            Log.d("MEOW MEOW", e.toString())
            null
        }

    /**
     * similar to [getRandomMeals] but for several meals
     *
     * @param mutableMeals a list that it will append the results. The [Meal]s can be null
     */
    suspend fun getRandomMeals(mutableMeals: SnapshotStateList<Meal?>) {
        try {
            val url =
                URLBuilder()
                    .apply {
                        takeFrom(SPOON)
                        appendPathSegments("randomselection.php")
                    }.toString()

            Log.d("CRASHING_MEOW", url)
            val response = client.get(url)
            if (response.status.value == 200) {
                val rawJson = response.bodyAsText()
                Log.d("Client", "Raw JSON Response: $rawJson")

                val meals = Json.decodeFromString<MealResponse>(rawJson).meals
                if (meals == null) {
                    return
                }

                for (meal in meals) {
                    mutableMeals.add(meal)
                }
            }
        } catch (e: Exception) {
            Log.d("MEOW MEOW", e.toString())
        }
    }

    /**
     * given a category (e.g beef, vegan, ...) it will fetch the meals from that category
     * and append them to the list
     *
     * @param mutableMeals list to append the [Meal]s
     * @param category
     */
    suspend fun getMealByCategory(
        mutableMeals: SnapshotStateList<Meal?>,
        category: String,
    ) {
        try {
            val url =
                URLBuilder()
                    .apply {
                        takeFrom(SPOON)
                        appendPathSegments("filter.php")
                        parameters.append("c", category)
                    }.toString()

            val response = client.get(url)

            if (response.status.value == 200) {
                val rawJson = response.bodyAsText()
                Log.d("Client", "Raw JSON Response: $rawJson")

                val meals = Json.decodeFromString<MealResponse>(rawJson).meals
                if (meals == null) {
                    return
                }

                for (meal in meals) {
                    mutableMeals.add(meal)
                }
            }
        } catch (e: Exception) {
            Log.e("GET Meal by category", e.toString())
        }
    }

    /**
     * gets a list of [Meal]s that have the ingredient that the user chose
     *
     * @param mutableMeals to append the [Meal]s to
     * @param ingredient the name of the ingredient
     */
    suspend fun getMealByIngredient(
        mutableMeals: SnapshotStateList<Meal?>,
        ingredient: String,
    ) {
        try {
            val url =
                URLBuilder()
                    .apply {
                        takeFrom(SPOON)
                        appendPathSegments("filter.php")
                        parameters.append("i", ingredient)
                    }.toString()

            val response = client.get(url)
            if (response.status.value == 200) {
                val rawJson = response.bodyAsText()
                val meals = Json.decodeFromString<MealResponse>(rawJson).meals
                if (meals == null) {
                    return
                }

                for (meal in meals) {
                    mutableMeals.add(meal)
                }
            }
        } catch (e: Exception) {
            Log.e("Getting meal by ingredient", e.toString())
        }
    }

    /**
     * returns the ingredients seen on the image captured/uplaoded by the user
     *
     * @param imageUri captured/uploaded image
     *
     * @return guessed ingredients
     */
    suspend fun getIngredientsFromImage(imageUri: Uri): List<String> = TODO("Not implemented yet!")
}
