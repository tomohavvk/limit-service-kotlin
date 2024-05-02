package com.tomohavvk.limit.persistence

import com.tomohavvk.limit.AppFlow
import com.tomohavvk.limit.persistence.entity.TransactionEntity
import reactor.core.publisher.Mono

interface TransactionRepository {
    fun insert(entity: TransactionEntity): Mono<AppFlow<Boolean>>
}