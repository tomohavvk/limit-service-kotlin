package com.tomohavvk.limit.service

import com.tomohavvk.limit.AppFlow
import com.tomohavvk.limit.protocol.request.CreateLimitRequest
import com.tomohavvk.limit.protocol.view.LimitView

interface LimitsService {
    suspend fun create(request: CreateLimitRequest): AppFlow<LimitView>

    suspend fun findAll(): AppFlow<List<LimitView>>
}