package com.tomohavvk.limit.web


import com.tomohavvk.limit.AppFlow
import com.tomohavvk.limit.protocol.CheckResult
import com.tomohavvk.limit.protocol.request.CheckRequest
import com.tomohavvk.limit.service.CheckService
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.awaitBody


@Component
class CheckHandlers(
    val service: CheckService
) {
    suspend fun check(req: ServerRequest): AppFlow<List<CheckResult>> {
        return service.check(req.awaitBody(CheckRequest::class))
    }
}