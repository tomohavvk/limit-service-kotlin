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
    val terminalId: UUID,
    val amount: Int,
    val currency: String,
    val cardHash: String,
    val mcc: String,
    val gateway: String,
    val processedAt: Long
) {
    @kotlinx.serialization.Transient
    val schema = mapOf(
        "uuid" to Schema(this.uuid, "uuid"),
        "terminalId" to Schema(this.terminalId, "uuid"),
        "amount" to Schema(this.amount, "numeric"),
        "currency" to Schema(this.currency, "varchar"),
        "cardHash" to Schema(this.cardHash, "varchar"),
        "mcc" to Schema(this.mcc, "varchar"),
        "gateway" to Schema(this.gateway, "varchar")
    )
}


data class Schema(val value: Any, val sqlType: String)
