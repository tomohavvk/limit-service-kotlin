@file:UseSerializers(
    NonEmptySetSerializer::class
)

package com.tomohavvk.limit.protocol

import arrow.core.NonEmptySet
import arrow.core.serialization.NonEmptySetSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers

class Common {
    enum class Aggregate {
        Sum,
        Count
    }

    @Serializable
    data class LimitOn(val property: String, val aggregate: Aggregate)

    @Serializable
    data class Filter(val property: String, val values: NonEmptySet<String>)

    @Serializable
    data class Criterion(val uuid: String, val limit: Int, val interval: Int)
}