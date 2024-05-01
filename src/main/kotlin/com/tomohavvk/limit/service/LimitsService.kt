package com.tomohavvk.limit.service

import arrow.core.Either
import com.tomohavvk.limit.error.AppError
import com.tomohavvk.limit.protocol.CreateLimitRequest
import com.tomohavvk.limit.protocol.CreateLimitResponse

interface LimitsService {
    suspend fun create(request: CreateLimitRequest): Either<AppError, CreateLimitResponse>
}