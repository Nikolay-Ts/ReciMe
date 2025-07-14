package com.sonnenstahl.recime.utils

import android.util.Log
import com.sonnenstahl.recime.BuildConfig
import com.sonnenstahl.recime.utils.data.MealResponse
import com.sonnenstahl.recime.utils.data.RandomMeal
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.http.URLBuilder
import io.ktor.http.Url
import io.ktor.http.appendPathSegments
import io.ktor.http.takeFrom
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.statement.bodyAsText
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

object Client {
    val spoon = BuildConfig.SPOON
    val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                prettyPrint = true
                isLenient = true
            })
        }
    }

    suspend fun getRandomRecipe(): RandomMeal? {
        return try {
            val url = URLBuilder().apply {
                takeFrom(spoon)
                appendPathSegments("random.php")
            }.toString()

            Log.d("CRASHING_MEOW", url)
            val response  = client.get(url)


            if (response.status.value == 200) {
                val rawJson = response.bodyAsText()
                Log.d("Client", "Raw JSON Response: $rawJson")

                Json.decodeFromString<MealResponse>(rawJson).meals?.firstOrNull()
            } else {
                null
            }

        } catch(e: Exception) {
            Log.d("MEOW MEOW", e.toString())
            null
        }

    }

}