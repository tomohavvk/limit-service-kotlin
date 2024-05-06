@file:UseSerializers(
    NonEmptySetSerializer::class,
    UUIDSerializer::class
)

package com.tomohavvk.limit.protocol

import arrow.core.NonEmptySet
import arrow.core.serialization.NonEmptySetSerializer
import com.tomohavvk.limit.serializers.UUIDSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import java.util.*

class Common {
    enum class Aggregate {
        Sum,
        Count
    }

    @Serializable
    data class Currency(val property: String, val values: NonEmptySet<String>)

    @Serializable
    data class LimitOn(val property: String, val aggregate: Aggregate)

    @Serializable
    data class Filter(val property: String, val values: NonEmptySet<String>)

    @Serializable
    data class Criterion(val uuid: UUID, val limit: Int, val interval: Int)
}