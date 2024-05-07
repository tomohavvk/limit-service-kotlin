package com.tomohavvk.limit.persistence

import arrow.core.Either
import arrow.core.serialization.NonEmptySetSerializer
import com.tomohavvk.limit.AppFlow
import com.tomohavvk.limit.persistence.entity.LimitEntity
import com.tomohavvk.limit.protocol.Common.Criterion
import com.tomohavvk.limit.protocol.Common.StaticFilter
import io.r2dbc.spi.ConnectionFactory
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono
import java.time.LocalDateTime
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

    // @formatter:off
    override fun insert(entity: LimitEntity): Mono<AppFlow<Boolean>> {
        val query =
            """insert into limits (uuid, name, description, limit_on, currencies, dynamic_filter, static_filter, criteria, created_at) 
                values (:uuid, :name, :description, :limitOn, :currencies, :dynamicFilter, :staticFilter, :criteria, :createdAt)"""

        return db.sql(query)
            .bind("uuid", entity.uuid)
            .bind("name", entity.name)
            .bind("description", entity.description)
            .bind("limitOn", io.r2dbc.postgresql.codec.Json.of(Json.encodeToString(entity.limitOn)))
            .bind("currencies", io.r2dbc.postgresql.codec.Json.of(Json.encodeToString(NonEmptySetSerializer(String.serializer()), entity.currencies)))
            .bind("dynamicFilter", io.r2dbc.postgresql.codec.Json.of(Json.encodeToString(NonEmptySetSerializer(String.serializer()), entity.dynamicFilter)))
            .bind("staticFilter", io.r2dbc.postgresql.codec.Json.of(Json.encodeToString(NonEmptySetSerializer(StaticFilter.serializer()), entity.staticFilter)))
            .bind("criteria", io.r2dbc.postgresql.codec.Json.of(Json.encodeToString(NonEmptySetSerializer(Criterion.serializer()), entity.criteria)))
            .bind("createdAt", entity.createdAt)
            .fetch().rowsUpdated()
            .map<AppFlow<Boolean>> { rowsUpdated -> Either.Right(rowsUpdated != 0L) }
            .onErrorResume { error -> handleError(error) }
    }

    override fun findAll(): Mono<AppFlow<List<LimitEntity>>> {
        val query =
            """select uuid, name, description, limit_on, currencies, dynamic_filter, static_filter, criteria, created_at from limits"""

        return db.sql(query)
            .map { row ->
                LimitEntity(
                    uuid = row.get("uuid", UUID::class.java),
                    name = row.get("name", String::class.java),
                    description = row.get("description", String::class.java),
                    currencies = Json.decodeFromString(NonEmptySetSerializer(String.serializer()), row.get("currencies", String::class.java)),
                    limitOn = Json.decodeFromString(row.get("limit_on", String::class.java)),
                    dynamicFilter = Json.decodeFromString(NonEmptySetSerializer(String.serializer()), row.get("dynamic_filter", String::class.java)),
                    staticFilter = Json.decodeFromString(NonEmptySetSerializer(StaticFilter.serializer()), row.get("static_filter", String::class.java)),
                    criteria = Json.decodeFromString(NonEmptySetSerializer(Criterion.serializer()), row.get("criteria", String::class.java)),
                    createdAt = row.get("created_at", LocalDateTime::class.java)
                )
            }
            .all()
            .collectList()
            .map<AppFlow<List<LimitEntity>>> { x -> Either.Right(x.toList()) }
            .onErrorResume { error -> handleError(error) }
    }
    // @formatter:on

}
