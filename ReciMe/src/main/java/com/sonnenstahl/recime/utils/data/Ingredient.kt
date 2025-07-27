package com.sonnenstahl.recime.utils.data

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.util.UUID

/**
 * data class to represent the ingredients in the fridge
 *
 * @param id UUID to parse it to the LazyVerticalGrid
 * @param name given by the user or added from the recipes, can have typos or be in plural
 * @param isSelected which ingredients to use for the query, cannot be more than 10
 * @param filePath of the image. There will be an algorithm to map the word to the closest image
 */
@Serializable
data class Ingredient(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    @Serializable(with = MutableBooleanStateSerializer::class)
    var isSelected: MutableState<Boolean> = mutableStateOf(false),
    val filePath: String = "file:///android_asset/default-ingredient.png",
)

object MutableBooleanStateSerializer : KSerializer<MutableState<Boolean>> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("MutableBooleanState", PrimitiveKind.BOOLEAN)

    override fun serialize(
        encoder: Encoder,
        value: MutableState<Boolean>,
    ) {
        encoder.encodeBoolean(value.value)
    }

    override fun deserialize(decoder: Decoder): MutableState<Boolean> = mutableStateOf(decoder.decodeBoolean())
}
