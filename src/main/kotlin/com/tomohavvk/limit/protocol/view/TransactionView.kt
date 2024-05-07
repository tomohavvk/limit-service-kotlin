@file:UseSerializers(
    NonEmptySetSerializer::class,
    UUIDSerializer::class
)

package com.tomohavvk.limit.protocol.view

import arrow.core.serialization.NonEmptySetSerializer
import com.tomohavvk.limit.persistence.entity.TransactionEntity
import com.tomohavvk.limit.serializers.UUIDSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import java.util.*


@Serializable
data class TransactionView(
    val uuid: UUID,
    val terminal: UUID,
    val amount: Int,
    val currency: String,
    val cardHash: String,
    val mcc: String,
    val gateway: String,
    val processedAt: Long
) {
    companion object {
        fun fromEntity(entity: TransactionEntity): TransactionView {
            return TransactionView(
                uuid = entity.uuid,
                terminal = entity.terminal,
                amount = entity.amount,
                currency = entity.currency,
                cardHash = entity.cardHash,
                mcc = entity.mcc,
                gateway = entity.gateway,
                processedAt = entity.processedAt
            )
        }
    }
}