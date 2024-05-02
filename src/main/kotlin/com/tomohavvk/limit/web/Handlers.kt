package com.tomohavvk.limit.web


import arrow.core.Either
import arrow.core.flatMap
import com.tomohavvk.limit.AppFlow
import com.tomohavvk.limit.error.ValidationError
import com.tomohavvk.limit.protocol.CreateLimitRequest
import com.tomohavvk.limit.protocol.LimitView
import com.tomohavvk.limit.service.LimitsService
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.awaitBody

@Component
class CreateLimitRequestValidator {
    fun validate(request: CreateLimitRequest): AppFlow<CreateLimitRequest> {
        return if (request.name.isBlank())
            Either.Left(ValidationError("The name should not be empty"))
        else if (request.description.isBlank())
            Either.Left(ValidationError("The description should not be empty"))
        else
            Either.Right(request)
    }
}

@Component
class Handlers(
    val service: LimitsService,
    val validator: CreateLimitRequestValidator,
) {
    suspend fun createLimit(req: ServerRequest): AppFlow<LimitView> {
        return validator.validate(req.awaitBody(CreateLimitRequest::class))
            .flatMap { validated -> service.create(validated) }
    }

    suspend fun findAll(): AppFlow<List<LimitView>> {
        return service.findAll()
    }

}