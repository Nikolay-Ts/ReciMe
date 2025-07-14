package com.sonnenstahl.recime.utils

import android.util.Log
import com.sonnenstahl.recime.BuildConfig
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.http.URLBuilder
import io.ktor.http.Url
import io.ktor.http.appendPathSegments
import io.ktor.http.takeFrom

object Client {
    val spoon = BuildConfig.SPOON
    val client = HttpClient(CIO) {}

    suspend fun getRandomRecipe(): Int {
        return try {
            val url = URLBuilder().apply {
                takeFrom(spoon)
                appendPathSegments("search.php?s=Arrabiata")
            }.toString()

            Log.d("CRASHING_MEOW", url)

            client.get(url)

            200
        } catch(e: Exception) {
            Log.d("MEOW MEOW", e.toString())
            -1
        }



    }

}