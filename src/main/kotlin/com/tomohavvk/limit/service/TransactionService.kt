package com.tomohavvk.limit.service

import arrow.core.Either
import com.tomohavvk.limit.AppFlow
import com.tomohavvk.limit.error.AppError
import com.tomohavvk.limit.persistence.TransactionRepository
import com.tomohavvk.limit.persistence.entity.TransactionEntity
import com.tomohavvk.limit.protocol.request.PersistTransactionRequest
import com.tomohavvk.limit.protocol.view.TransactionView
import kotlinx.coroutines.reactor.awaitSingle
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service


interface TransactionService {
    suspend fun persist(request: PersistTransactionRequest): AppFlow<TransactionView>
}

@Service
class TransactionServiceImpl(val transactionRepository: TransactionRepository) : TransactionService {
    private val log: Logger = LoggerFactory.getLogger(this.javaClass)

    override suspend fun persist(request: PersistTransactionRequest): Either<AppError, TransactionView> {
        val entity = TransactionEntity.fromRequest(request)

        return transactionRepository.insert(entity)
            .map { res -> res.map { TransactionView.fromEntity(entity) } }
            .awaitSingle()
    }
}