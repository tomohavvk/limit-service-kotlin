package com.tomohavvk.limit.persistence

import arrow.core.Either
import com.tomohavvk.limit.AppFlow
import com.tomohavvk.limit.protocol.CheckResult
import io.r2dbc.spi.ConnectionFactory
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono
import java.util.*

interface CheckRepository {
    fun check(query: String): Mono<AppFlow<List<CheckResult>>>
}

@Repository
class CheckRepositoryRepositoryImpl(connectionFactory: ConnectionFactory) : CheckRepository, DatabaseErrorHandler {
    private var db: DatabaseClient = DatabaseClient.create(connectionFactory)

    override val log: Logger = LoggerFactory.getLogger(this.javaClass)

    init {
        this.db = DatabaseClient.create(connectionFactory)
    }

    override fun check(query: String): Mono<AppFlow<List<CheckResult>>> {

        return db.sql(query)
            .map { row ->
                CheckResult(
                    limitUuid = row.get("limitUuid", UUID::class.java),
                    criterionId = row.get("criterionId", UUID::class.java)
                )
            }
            .all()
            .collectList()
            .map<AppFlow<List<CheckResult>>> { x -> Either.Right(x.toList()) }
            .onErrorResume { error -> handleError(error) }
    }
}
