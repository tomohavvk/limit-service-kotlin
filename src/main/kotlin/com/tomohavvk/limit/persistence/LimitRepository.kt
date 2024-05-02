package com.tomohavvk.limit.persistence

import com.tomohavvk.limit.AppFlow
import com.tomohavvk.limit.persistence.entity.LimitEntity
import reactor.core.publisher.Mono

interface LimitRepository {
    fun insert(entity: LimitEntity): Mono<AppFlow<Boolean>>
    fun findAll(): Mono<AppFlow<List<LimitEntity>>>
}