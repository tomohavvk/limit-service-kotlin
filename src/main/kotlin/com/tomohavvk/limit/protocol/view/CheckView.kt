@file:UseSerializers(
    NonEmptySetSerializer::class,
    UUIDSerializer::class
)

package com.tomohavvk.limit.protocol.view

import arrow.core.serialization.NonEmptySetSerializer
import com.tomohavvk.limit.protocol.CheckResult
import com.tomohavvk.limit.serializers.UUIDSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers

@Serializable
data class CheckView(
    val isLimited: Boolean,
    val limitedBy: List<CheckResult>
)