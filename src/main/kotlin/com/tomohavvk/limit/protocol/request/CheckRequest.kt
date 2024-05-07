@file:UseSerializers(
    NonEmptySetSerializer::class,
    UUIDSerializer::class
)

package com.tomohavvk.limit.protocol.request

import arrow.core.serialization.NonEmptySetSerializer
import com.tomohavvk.limit.serializers.UUIDSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import java.util.*


@Serializable
data class CheckRequest(
    val uuid: UUID,
    val terminal: UUID,
    val amount: Int,
    val currency: String,
    val cardHash: String,
    val mcc: String,
    val gateway: String,
    val processedAt: Long
)

