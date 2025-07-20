package com.sonnenstahl.recime.utils.data

import coil3.Bitmap
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonPrimitive

@Serializable
data class MealResponse(
    val meals: List<Meal>?,
)

@Serializable(with = Meal.Serializer::class)
data class Meal(
    val idMeal: String,
    val strMeal: String,
    val strMealAlternate: String? = null,
    val strCategory: String? = null,
    val strArea: String? = null,
    val strInstructions: String? = null,
    val strMealThumb: String? = null,
    val strTags: String? = null,
    val strYoutube: String? = null,
    val ingredients: List<String>? = null,
    val measures: List<String>? = null,
    val strSource: String? = null,
    val strImageSource: String? = null,
    val strCreativeCommonsConfirmed: String? = null,
    val dateModified: String? = null,
) {
    companion object Serializer : KSerializer<Meal> {
        override val descriptor: SerialDescriptor =
            buildClassSerialDescriptor("Meal") {
                element(
                    "idMeal",
                    kotlinx.serialization.descriptors.PrimitiveSerialDescriptor(
                        "idMeal",
                        kotlinx.serialization.descriptors.PrimitiveKind.STRING,
                    ),
                )
                element(
                    "strMeal",
                    kotlinx.serialization.descriptors.PrimitiveSerialDescriptor(
                        "strMeal",
                        kotlinx.serialization.descriptors.PrimitiveKind.STRING,
                    ),
                )
            }

        override fun deserialize(decoder: Decoder): Meal {
            val jsonDecoder = decoder as JsonDecoder
            val jsonObject = jsonDecoder.decodeJsonElement() as JsonObject

            val ingredientsList = mutableListOf<String>()
            val measuresList = mutableListOf<String>()

            for (i in 1..20) {
                val ingredientKey = "strIngredient$i"
                val measureKey = "strMeasure$i"

                val ingredient = jsonObject[ingredientKey]?.jsonPrimitive?.contentOrNull
                val measure = jsonObject[measureKey]?.jsonPrimitive?.contentOrNull

                if (!ingredient.isNullOrBlank()) {
                    ingredientsList.add(ingredient)
                }
                if (!measure.isNullOrBlank()) {
                    measuresList.add(measure)
                }
            }

            return Meal(
                idMeal = jsonObject["idMeal"]?.jsonPrimitive?.content ?: "",
                strMeal = jsonObject["strMeal"]?.jsonPrimitive?.content ?: "",
                strMealAlternate = jsonObject["strMealAlternate"]?.jsonPrimitive?.contentOrNull,
                strCategory = jsonObject["strCategory"]?.jsonPrimitive?.contentOrNull,
                strArea = jsonObject["strArea"]?.jsonPrimitive?.contentOrNull,
                strInstructions = jsonObject["strInstructions"]?.jsonPrimitive?.contentOrNull,
                strMealThumb = jsonObject["strMealThumb"]?.jsonPrimitive?.contentOrNull,
                strTags = jsonObject["strTags"]?.jsonPrimitive?.contentOrNull,
                strYoutube = jsonObject["strYoutube"]?.jsonPrimitive?.contentOrNull,
                ingredients = if (ingredientsList.isEmpty()) null else ingredientsList,
                measures = if (measuresList.isEmpty()) null else measuresList,
                strSource = jsonObject["strSource"]?.jsonPrimitive?.contentOrNull,
                strImageSource = jsonObject["strImageSource"]?.jsonPrimitive?.contentOrNull,
                strCreativeCommonsConfirmed = jsonObject["strCreativeCommonsConfirmed"]?.jsonPrimitive?.contentOrNull,
                dateModified = jsonObject["dateModified"]?.jsonPrimitive?.contentOrNull,
            )
        }

        override fun serialize(
            encoder: Encoder,
            value: Meal,
        ): Unit = throw NotImplementedError("Serialization for Meal is not implemented")
    }
}
