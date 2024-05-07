@file:UseSerializers(
    NonEmptySetSerializer::class,
    UUIDSerializer::class
)

package com.tomohavvk.limit.persistence.entity


import arrow.core.NonEmptySet
import arrow.core.serialization.NonEmptySetSerializer
import com.tomohavvk.limit.protocol.Common.*
import com.tomohavvk.limit.protocol.request.CreateLimitRequest
import com.tomohavvk.limit.serializers.UUIDSerializer
import kotlinx.serialization.UseSerializers
import java.time.LocalDateTime
import java.util.*


data class LimitEntity(
    val uuid: UUID,
    val name: String,
    val description: String,
    val limitOn: LimitOn,
    val currencies: NonEmptySet<String>,
    val dynamicFilter: NonEmptySet<String>,
    val staticFilter: NonEmptySet<StaticFilter>,
    val criteria: NonEmptySet<Criterion>,
    val createdAt: LocalDateTime
) {
    companion object {
        fun fromRequest(uuid: UUID, request: CreateLimitRequest): LimitEntity {
            return LimitEntity(
                uuid = uuid,
                name = request.name,
                description = request.description,
                limitOn = request.limitOn,
                currencies = request.currencies,
                dynamicFilter = request.dynamicFilter,
                staticFilter = request.staticFilter,
                criteria = request.criteria,
                createdAt = LocalDateTime.now()
            )
        }
    }
}
