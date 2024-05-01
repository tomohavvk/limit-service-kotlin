package com.tomohavvk.limit.web


import arrow.core.Either
import com.tomohavvk.limit.error.AppError
import com.tomohavvk.limit.error.ValidationError
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.bodyValueAndAwait
import org.springframework.web.reactive.function.server.coRouter

@Configuration
class Routers(val handlers: Handlers) {

    @Bean
    fun api() = coRouter {
        "/api/v1/".nest {
            accept(APPLICATION_JSON).nest {
                POST("limits") { request -> handleResponse(HttpStatus.CREATED, handlers.createLimit(request)) }
            }
        }
    }

    suspend fun <T : Any> handleResponse(statusIfOk: HttpStatusCode, either: Either<AppError, T>): ServerResponse {
        return either.fold({ error ->
            val statusCode = when (error) {
                is ValidationError -> HttpStatus.BAD_REQUEST
                else -> HttpStatus.INTERNAL_SERVER_ERROR
            }

            ServerResponse.status(statusCode).contentType(APPLICATION_JSON)
                .bodyValueAndAwait(mapOf("error" to error.reason))
        }, { request ->

            ServerResponse.status(statusIfOk).contentType(APPLICATION_JSON)
                .bodyValueAndAwait(request)
        })
    }
}

