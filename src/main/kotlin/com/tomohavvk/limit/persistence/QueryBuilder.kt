package com.tomohavvk.limit.persistence

import com.tomohavvk.limit.persistence.entity.LimitEntity
import com.tomohavvk.limit.protocol.Common
import com.tomohavvk.limit.protocol.request.CheckRequest
import com.tomohavvk.limit.protocol.request.Schema
import io.zeko.db.sql.Query
import io.zeko.db.sql.aggregations.countGe
import io.zeko.db.sql.aggregations.sumGe
import io.zeko.db.sql.dsl.and
import io.zeko.db.sql.dsl.greaterEq
import io.zeko.db.sql.operators.eq
import io.zeko.db.sql.operators.inList
import org.springframework.stereotype.Component

interface QueryBuilder {
    fun build(checkRequest: CheckRequest, limits: List<LimitEntity>): String
}

@Component
class QueryBuilderImpl : QueryBuilder {
    private val now = "NOW()"
    private val timeColumn = "processedAt"

    override fun build(checkRequest: CheckRequest, limits: List<LimitEntity>): String {

        val queries = limits.flatMap { limit ->
            limit.limit.criteria.map { criterion ->

                val aggregate = if (limit.limit.limitOn.aggregate == Common.Aggregate.Count)
                    countGe(limit.limit.limitOn.property, criterion.limit)
                else
                    sumGe(limit.limit.limitOn.property, criterion.limit)

                Query()
                    .from("transactions")
                    .fields(
                        "${quoted(limit.uuid)}::uuid as limitUuid",
                        "${quoted(criterion.uuid)}::uuid as criterionId"
                    )
                    .where(
                        inList(limit.limit.currency.property, limit.limit.currency.values.toList(), true),
                        (limit.limit.filterBy.map { filter -> inList(filter.property, filter.values.toList(), true) }
                            .reduce { x, y -> x and y }),
                        limit.limit.groupBy.map { property ->
                            eq(
                                property,
                                mbQuotedValue(checkRequest.schema.getValue(property)),
                                true
                            )
                        }.reduce { x, y -> x and y },
                        timeColumn greaterEq "($now - INTERVAL ${quoted("${criterion.interval} days")})"
                    )
                    .having(aggregate)
                    .toSql(shouldLineBreak = true)
            }
        }

        val united = queries.toList().joinToString("\nUNION ALL\n", transform = { s -> "($s)" })
        return united
    }

    private fun quoted(value: Any): String {
        return "'$value'"
    }

    private fun mbQuotedValue(schema: Schema): String {
        return if (listOf("varchar", "uuid").contains(schema.sqlType)) {
            quoted(schema.value)
        } else schema.value.toString()
    }

}

