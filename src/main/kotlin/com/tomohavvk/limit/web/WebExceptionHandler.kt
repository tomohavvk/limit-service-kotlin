package com.tomohavvk.limit.web

import kotlinx.serialization.SerializationException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.web.WebProperties
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler
import org.springframework.boot.web.reactive.error.ErrorAttributes
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.http.MediaType
import org.springframework.http.codec.ServerCodecConfigurer
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.server.*
import reactor.core.publisher.Mono

@Component
private class Deps {
    @Bean
    fun webResources(): WebProperties.Resources {
        return WebProperties.Resources()
    }
}

@Component
class WebExceptionHandler(
    errorAttributes: ErrorAttributes?,
    resources: WebProperties.Resources?,
    applicationContext: ApplicationContext?,
    configurer: ServerCodecConfigurer?
) : AbstractErrorWebExceptionHandler(errorAttributes, resources, applicationContext) {
    private val log: Logger = LoggerFactory.getLogger(this.javaClass)

    private data class ErrorWithCode(val error: String, val code: Int)

    init {
        configurer?.let {
            this.setMessageWriters(configurer.writers)
        }
    }

    override fun getRoutingFunction(errorAttributes: ErrorAttributes): RouterFunction<ServerResponse> {

        return RouterFunctions.route(RequestPredicates.all(), this::renderErrorResponse)
    }

    fun renderErrorResponse(request: ServerRequest?): Mono<ServerResponse> {
        val error = super.getError(request)

        log.error(request!!.path(), error)

        val errorMessage = when (error) {
            is SerializationException -> ErrorWithCode(error.message ?: "unknown error", 400)
            else -> ErrorWithCode("Internal Server Error", 500)
        }

        return ServerResponse.status(errorMessage.code).contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(mapOf("error" to errorMessage.error)))
    }

}