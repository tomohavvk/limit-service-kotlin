package com.tomohavvk.limit.web

import com.tomohavvk.limit.AppFlow
import com.tomohavvk.limit.protocol.request.PersistTransactionRequest
import com.tomohavvk.limit.protocol.view.TransactionView
import com.tomohavvk.limit.service.TransactionService
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.awaitBody


@Component
class TransactionHandlers(
    val service: TransactionService
) {
    suspend fun persist(req: ServerRequest): AppFlow<TransactionView> {
        val transaction = req.awaitBody(PersistTransactionRequest::class)

        return service.persist(transaction)
    }
}