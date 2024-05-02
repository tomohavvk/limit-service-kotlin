package com.tomohavvk.limit.persistence.entity

import java.util.*

data class LimitEntity(
    val uuid: UUID,
    val limit: Limit
)