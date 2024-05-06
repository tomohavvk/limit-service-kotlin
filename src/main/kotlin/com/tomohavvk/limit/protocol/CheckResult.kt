@file:UseSerializers(
    UUIDSerializer::class
)

package com.tomohavvk.limit.protocol

import com.tomohavvk.limit.serializers.UUIDSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import java.util.*

@Serializable
data class CheckResult(val limitUuid: UUID, val criterionId: UUID)