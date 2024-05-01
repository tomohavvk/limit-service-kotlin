@file:UseSerializers(
    NonEmptySetSerializer::class
)

package com.tomohavvk.limit.protocol

import arrow.core.NonEmptySet
import arrow.core.serialization.NonEmptySetSerializer
import com.tomohavvk.limit.protocol.Common.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers


@Serializable
data class CreateLimitRequest(
    val name: String,
    val description: String,
    val limitOn: LimitOn,
    val groupBy: NonEmptySet<String>,
    val filterBy: NonEmptySet<Filter>,
    val criteria: NonEmptySet<Criterion>
)