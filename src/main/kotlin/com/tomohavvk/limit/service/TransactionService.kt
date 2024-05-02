package com.tomohavvk.limit.service

import com.tomohavvk.limit.AppFlow
import com.tomohavvk.limit.protocol.request.PersistTransactionRequest
import com.tomohavvk.limit.protocol.view.TransactionView

interface TransactionService {
    suspend fun persist(request: PersistTransactionRequest): AppFlow<TransactionView>
}