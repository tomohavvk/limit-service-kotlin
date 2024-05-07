@file:UseSerializers(
    NonEmptySetSerializer::class
)

package com.tomohavvk.limit.protocol.request

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
    val currencies: NonEmptySet<String>,
    val dynamicFilter: NonEmptySet<String>,
    val staticFilter: NonEmptySet<StaticFilter>,
    val criteria: NonEmptySet<Criterion>
)