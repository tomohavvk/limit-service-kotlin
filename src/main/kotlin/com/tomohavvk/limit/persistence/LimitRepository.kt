package com.tomohavvk.limit.persistence

import arrow.core.Either
import com.tomohavvk.limit.AppFlow
import com.tomohavvk.limit.persistence.entity.LimitEntity
import io.r2dbc.spi.ConnectionFactory
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono
import java.util.*

interface LimitRepository {
    fun insert(entity: LimitEntity): Mono<AppFlow<Boolean>>
    fun findAll(): Mono<AppFlow<List<LimitEntity>>>
}

@Repository
class LimitRepositoryImpl(connectionFactory: ConnectionFactory) : LimitRepository, DatabaseErrorHandler {
    private var db: DatabaseClient = DatabaseClient.create(connectionFactory)

    override val log: Logger = LoggerFactory.getLogger(this.javaClass)

    init {
        this.db = DatabaseClient.create(connectionFactory)
    }

    override fun insert(entity: LimitEntity): Mono<AppFlow<Boolean>> {
        val query = """insert into limits (uuid, "limit") values (:uuid, :limit)"""

        return db.sql(query).bind("uuid", entity.uuid)
            .bind("limit", io.r2dbc.postgresql.codec.Json.of(Json.encodeToString(entity.limit)))
            .fetch().rowsUpdated()
            .map<AppFlow<Boolean>> { rowsUpdated -> Either.Right(rowsUpdated != 0L) }
            .onErrorResume { error -> handleError(error) }
    }

    override fun findAll(): Mono<AppFlow<List<LimitEntity>>> {
        val query = """select uuid, "limit" from limits"""

        return db.sql(query)
            .map { row ->
                LimitEntity(
                    uuid = row.get("uuid", UUID::class.java),
                    limit = Json.decodeFromString(row.get("limit", String::class.java))
                )
            }
            .all()
            .collectList()
            .map<AppFlow<List<LimitEntity>>> { x -> Either.Right(x.toList()) }
            .onErrorResume { error -> handleError(error) }
    }

}
