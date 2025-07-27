package com.sonnenstahl.recime.utils.data

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonPrimitive

@Serializable
data class MealResponse(
    val meals: List<Meal>?,
)

/**
 * a representation of the json response from the API a lot of the main properties are listed
 *
 * @param idMeal used to search and query more info about the meal
 * @param strMeal name of the meal, can be used the same way as idMeal
 * @param strMealThumb the image url. A separate request must be done to obtain the actual image]
 * @param ingredients which the user can add to the Fridge
 */
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
    val ingredients: MutableList<String>? = null,
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
        ) {
            val jsonEncoder = encoder as kotlinx.serialization.json.JsonEncoder
            jsonEncoder.encodeJsonElement(
                buildJsonObject {
                    put("idMeal", JsonPrimitive(value.idMeal))
                    put("strMeal", JsonPrimitive(value.strMeal))

                    value.strMealAlternate?.let { put("strMealAlternate", JsonPrimitive(it)) }
                    value.strCategory?.let { put("strCategory", JsonPrimitive(it)) }
                    value.strArea?.let { put("strArea", JsonPrimitive(it)) }
                    value.strInstructions?.let { put("strInstructions", JsonPrimitive(it)) }
                    value.strMealThumb?.let { put("strMealThumb", JsonPrimitive(it)) }
                    value.strTags?.let { put("strTags", JsonPrimitive(it)) }
                    value.strYoutube?.let { put("strYoutube", JsonPrimitive(it)) }
                    value.strSource?.let { put("strSource", JsonPrimitive(it)) }
                    value.strImageSource?.let { put("strImageSource", JsonPrimitive(it)) }
                    value.strCreativeCommonsConfirmed?.let { put("strCreativeCommonsConfirmed", JsonPrimitive(it)) }
                    value.dateModified?.let { put("dateModified", JsonPrimitive(it)) }

                    value.ingredients?.forEachIndexed { index, ingredient ->
                        put("strIngredient${index + 1}", JsonPrimitive(ingredient))
                    }
                    value.measures?.forEachIndexed { index, measure ->
                        put("strMeasure${index + 1}", JsonPrimitive(measure))
                    }
                },
            )
        }
    }
}
