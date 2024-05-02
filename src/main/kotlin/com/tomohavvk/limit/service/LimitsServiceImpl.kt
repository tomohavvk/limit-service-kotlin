package com.tomohavvk.limit.service

import arrow.core.Either
import com.tomohavvk.limit.AppFlow
import com.tomohavvk.limit.error.AppError
import com.tomohavvk.limit.persistence.LimitRepository
import com.tomohavvk.limit.persistence.entity.Limit
import com.tomohavvk.limit.persistence.entity.LimitEntity
import com.tomohavvk.limit.protocol.CreateLimitRequest
import com.tomohavvk.limit.protocol.LimitView
import kotlinx.coroutines.reactor.awaitSingle
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.util.*

@Service
class LimitsServiceImpl(val limitRepository: LimitRepository) : LimitsService {
    private val log: Logger = LoggerFactory.getLogger(this.javaClass)

    override suspend fun create(request: CreateLimitRequest): Either<AppError, LimitView> {
        val entity = LimitEntity(UUID.randomUUID(), Limit.fromRequest(request))

        return limitRepository.insert(entity)
            .map { res -> res.map { LimitView.fromEntity(entity) } }
            .awaitSingle()
    }

    override suspend fun findAll(): AppFlow<List<LimitView>> {
        return limitRepository.findAll()
            .awaitSingle()
            .map { result -> result.map { entity -> LimitView.fromEntity(entity) } }
    }
}