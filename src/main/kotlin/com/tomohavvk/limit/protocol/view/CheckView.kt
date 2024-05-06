@file:UseSerializers(
    NonEmptySetSerializer::class,
    UUIDSerializer::class
)

package com.tomohavvk.limit.protocol.view

import arrow.core.serialization.NonEmptySetSerializer
import com.tomohavvk.limit.serializers.UUIDSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import java.util.*

@Serializable
data class CheckView(
    val isLimited: Boolean,
    val limitedBy: List<UUID>
)