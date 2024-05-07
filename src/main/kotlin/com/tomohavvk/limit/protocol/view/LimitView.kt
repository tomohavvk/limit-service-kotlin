@file:UseSerializers(
    LocalDateTimeSerializer::class,
    NonEmptySetSerializer::class,
    UUIDSerializer::class
)

package com.tomohavvk.limit.protocol.view

import arrow.core.NonEmptySet
import arrow.core.serialization.NonEmptySetSerializer
import com.tomohavvk.limit.persistence.entity.LimitEntity
import com.tomohavvk.limit.protocol.Common.*
import com.tomohavvk.limit.serializers.LocalDateTimeSerializer
import com.tomohavvk.limit.serializers.UUIDSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import java.time.LocalDateTime
import java.util.*


@Serializable
data class LimitView(
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
        fun fromEntity(entity: LimitEntity): LimitView {
            return LimitView(
                uuid = entity.uuid,
                name = entity.name,
                description = entity.description,
                currencies = entity.currencies,
                limitOn = entity.limitOn,
                dynamicFilter = entity.dynamicFilter,
                staticFilter = entity.staticFilter,
                criteria = entity.criteria,
                createdAt = entity.createdAt
            )
        }
    }
}