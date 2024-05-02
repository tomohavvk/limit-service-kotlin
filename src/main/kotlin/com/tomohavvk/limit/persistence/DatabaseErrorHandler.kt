package com.tomohavvk.limit.persistence

import arrow.core.Either
import com.tomohavvk.limit.AppFlow
import com.tomohavvk.limit.error.DatabaseError
import com.tomohavvk.limit.error.NotUniqueError
import org.slf4j.Logger
import org.springframework.dao.DuplicateKeyException
import reactor.core.publisher.Mono


interface DatabaseErrorHandler {
    val log: Logger

    fun <T> handleError(error: Throwable): Mono<AppFlow<T>> {
        log.error("Database layer error: {}", error.message, error)

        return when (error) {
            is DuplicateKeyException ->
                Mono.just(Either.Left(NotUniqueError("Entity already exists in the system")))

            else -> Mono.just(Either.Left(DatabaseError("Database error")))
        }

    }
}