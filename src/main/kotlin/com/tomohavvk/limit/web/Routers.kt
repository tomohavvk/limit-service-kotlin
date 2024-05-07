package com.tomohavvk.limit.web


import com.tomohavvk.limit.AppFlow
import com.tomohavvk.limit.error.ValidationError
import com.tomohavvk.limit.protocol.view.LimitView
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.bodyValueAndAwait
import org.springframework.web.reactive.function.server.coRouter

@Configuration
class Routers(
    val limitHandlers: LimitHandlers,
    val transactionHandlers: TransactionHandlers,
    val checkHandlers: CheckHandlers
) {

    @Bean
    fun api() = coRouter {
        "/api/v1/limits".nest {
            accept(APPLICATION_JSON).nest {
                POST("") { request ->
                    handleResponse(HttpStatus.CREATED, limitHandlers.create(request))
                }
            }

            accept(APPLICATION_JSON).nest {
                GET("") { _ ->
                    val limits = limitHandlers.findAll()
                        .map { list -> Json.encodeToString(ListSerializer(LimitView.serializer()), list) }

                    handleResponse(HttpStatus.OK, limits)
                }
            }
        }

        "/api/v1/transactions".nest {
            accept(APPLICATION_JSON).nest {
                POST("") { request ->
                    handleResponse(HttpStatus.CREATED, transactionHandlers.persist(request))
                }
            }

        }

        "/api/v1/check".nest {
            accept(APPLICATION_JSON).nest {
                POST("") { request ->
                    handleResponse(HttpStatus.OK, checkHandlers.check(request))
                }
            }

        }
    }

    suspend fun <T : Any> handleResponse(statusIfOk: HttpStatusCode, flow: AppFlow<T>): ServerResponse {
        return flow.fold({ error ->
            val statusCode = when (error) {
                is ValidationError -> HttpStatus.BAD_REQUEST
                else -> HttpStatus.INTERNAL_SERVER_ERROR
            }

            ServerResponse.status(statusCode).contentType(APPLICATION_JSON)
                .bodyValueAndAwait(mapOf("error" to error.reason))
        }, { body ->

            ServerResponse.status(statusIfOk).contentType(APPLICATION_JSON)
                .bodyValueAndAwait(body)
        })
    }
}

