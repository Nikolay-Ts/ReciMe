package com.sonnenstahl.recime.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.sonnenstahl.recime.BuildConfig
import com.sonnenstahl.recime.utils.data.MealResponse
import com.sonnenstahl.recime.utils.data.Meal
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

object Client {
    val spoon = BuildConfig.SPOON
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

    suspend fun getMealByName(mealName: String): Meal? =
        try {
            val url =
                URLBuilder()
                    .apply {
                        takeFrom(spoon)
                        appendPathSegments("search.php")
                        parameters.append("s", mealName)
                    }.toString()

            Log.d("GET MEAL BY NAME", url)

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
            Log.d("Could not GET:", e.toString())
            null
        }

    suspend fun getRandomMeal(): Meal? =
        try {
            val url =
                URLBuilder()
                    .apply {
                        takeFrom(spoon)
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

    suspend fun getRandomMeals(mutableMeals: SnapshotStateList<Meal?>) {
        try {
            val url =
                URLBuilder()
                    .apply {
                        takeFrom(spoon)
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
}
