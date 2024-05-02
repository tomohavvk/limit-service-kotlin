@file:UseSerializers(
    NonEmptySetSerializer::class,
    UUIDSerializer::class
)

package com.tomohavvk.limit.protocol.view

import arrow.core.NonEmptySet
import arrow.core.serialization.NonEmptySetSerializer
import com.tomohavvk.limit.persistence.entity.LimitEntity
import com.tomohavvk.limit.protocol.Common.*
import com.tomohavvk.limit.serializers.UUIDSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import java.util.*


@Serializable
data class LimitView(
    val uuid: UUID,
    val name: String,
    val description: String,
    val limitOn: LimitOn,
    val groupBy: NonEmptySet<String>,
    val filterBy: NonEmptySet<Filter>,
    val criteria: NonEmptySet<Criterion>
) {
    companion object {
        fun fromEntity(entity: LimitEntity): LimitView {
            return LimitView(
                uuid = entity.uuid,
                name = entity.limit.name,
                description = entity.limit.description,
                limitOn = entity.limit.limitOn,
                groupBy = entity.limit.groupBy,
                filterBy = entity.limit.filterBy,
                criteria = entity.limit.criteria
            )
        }
    }
}