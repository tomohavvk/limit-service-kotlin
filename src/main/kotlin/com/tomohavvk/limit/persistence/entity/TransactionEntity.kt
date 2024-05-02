package com.tomohavvk.limit.persistence.entity

import com.tomohavvk.limit.protocol.request.PersistTransactionRequest

import java.util.*


data class TransactionEntity(
    val uuid: UUID,
    val terminalId: UUID,
    val amount: Int,
    val currency: String,
    val cardHash: String,
    val mcc: String,
    val gateway: String,
    val processedAt: Long
) {
    companion object {
        fun fromRequest(request: PersistTransactionRequest): TransactionEntity {
            return TransactionEntity(
                uuid = request.uuid,
                terminalId = request.terminalId,
                amount = request.amount,
                currency = request.currency,
                cardHash = request.cardHash,
                mcc = request.mcc,
                gateway = request.gateway,
                processedAt = request.processedAt
            )
        }
    }
}