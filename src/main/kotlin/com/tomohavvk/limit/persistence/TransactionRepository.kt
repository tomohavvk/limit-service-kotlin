package com.tomohavvk.limit.persistence

import arrow.core.Either
import com.tomohavvk.limit.AppFlow
import com.tomohavvk.limit.persistence.entity.TransactionEntity
import io.r2dbc.spi.ConnectionFactory
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono
import java.sql.Timestamp
import java.time.Instant

interface TransactionRepository {
    fun insert(entity: TransactionEntity): Mono<AppFlow<Boolean>>
}

@Repository
class TransactionRepositoryImpl(connectionFactory: ConnectionFactory) : TransactionRepository, DatabaseErrorHandler {
    private var db: DatabaseClient = DatabaseClient.create(connectionFactory)

    override val log: Logger = LoggerFactory.getLogger(this.javaClass)

    init {
        this.db = DatabaseClient.create(connectionFactory)
    }

    override fun insert(entity: TransactionEntity): Mono<AppFlow<Boolean>> {
        val query =
            """INSERT INTO transactions (uuid, terminalId, amount, currency, cardHash, mcc, gateway, processedAt)
                 VALUES (:uuid, :terminalId, :amount, :currency, :cardHash, :mcc, :gateway, :processedAt) 
                 ON CONFLICT (uuid) DO NOTHING"""

        return db.sql(query)
            .bind("uuid", entity.uuid)
            .bind("terminalId", entity.terminalId)
            .bind("amount", entity.amount)
            .bind("currency", entity.currency)
            .bind("cardHash", entity.cardHash)
            .bind("mcc", entity.mcc)
            .bind("gateway", entity.gateway)
            .bind("processedAt", Timestamp.from(Instant.ofEpochMilli(entity.processedAt)))
            .fetch().rowsUpdated()
            .map<AppFlow<Boolean>> { rowsUpdated -> Either.Right(rowsUpdated != 0L) }
            .onErrorResume { error -> handleError(error) }
    }
}
