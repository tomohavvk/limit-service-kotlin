@file:UseSerializers(
    NonEmptySetSerializer::class,
    UUIDSerializer::class
)

package com.tomohavvk.limit.protocol

import arrow.core.NonEmptySet
import arrow.core.serialization.NonEmptySetSerializer
import com.tomohavvk.limit.protocol.Common.*
import com.tomohavvk.limit.serializers.UUIDSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import java.util.*


@Serializable
data class CreateLimitResponse(
    val uuid: UUID,
    val name: String,
    val description: String,
    val limitOn: LimitOn,
    val groupBy: NonEmptySet<String>,
    val filterBy: NonEmptySet<Filter>,
    val criteria: NonEmptySet<Criterion>
) {
    companion object {
        fun fromRequest(uuid: UUID, request: CreateLimitRequest): CreateLimitResponse {
            return CreateLimitResponse(
                uuid = uuid,
                name = request.name,
                description = request.description,
                limitOn = request.limitOn,
                groupBy = request.groupBy,
                filterBy = request.filterBy,
                criteria = request.criteria
            )
        }
    }
}