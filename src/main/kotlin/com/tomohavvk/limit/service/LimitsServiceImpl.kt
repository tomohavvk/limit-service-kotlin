package com.tomohavvk.limit.service

import arrow.core.Either
import com.tomohavvk.limit.error.AppError
import com.tomohavvk.limit.protocol.CreateLimitRequest
import com.tomohavvk.limit.protocol.CreateLimitResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.util.*

@Service
class LimitsServiceImpl() : LimitsService {
    private val log: Logger = LoggerFactory.getLogger(this.javaClass)

    override suspend fun create(request: CreateLimitRequest): Either<AppError, CreateLimitResponse> {
        log.debug("Creating limit: {}", request)

        return Either.Right(CreateLimitResponse.fromRequest(UUID.randomUUID(), request))
    }
}