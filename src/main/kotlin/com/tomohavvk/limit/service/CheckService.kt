package com.tomohavvk.limit.service

import arrow.core.flatMap
import com.tomohavvk.limit.AppFlow
import com.tomohavvk.limit.persistence.CheckRepository
import com.tomohavvk.limit.persistence.LimitRepository
import com.tomohavvk.limit.persistence.QueryBuilder
import com.tomohavvk.limit.protocol.request.CheckRequest
import com.tomohavvk.limit.protocol.view.CheckView
import kotlinx.coroutines.reactor.awaitSingle
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

interface CheckService {
    suspend fun check(request: CheckRequest): AppFlow<CheckView>
}

@Service
class CheckServiceImpl(
    val limitRepository: LimitRepository,
    val checkRepository: CheckRepository,
    val queryBuilder: QueryBuilder
) : CheckService {
    private val log: Logger = LoggerFactory.getLogger(this.javaClass)

    override suspend fun check(request: CheckRequest): AppFlow<CheckView> {
        return limitRepository.findAll().awaitSingle().flatMap { limits ->

            val checkQuery = queryBuilder.build(request, limits)
            log.info(checkQuery)

            checkRepository.check(checkQuery).awaitSingle()
                .map { result ->
                    CheckView(isLimited = result.isNotEmpty(), result)
                }
        }
    }
}