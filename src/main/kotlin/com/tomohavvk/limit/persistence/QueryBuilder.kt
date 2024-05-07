package com.tomohavvk.limit.persistence

import com.tomohavvk.limit.persistence.entity.LimitEntity
import com.tomohavvk.limit.protocol.Common
import com.tomohavvk.limit.protocol.request.CheckRequest
import io.zeko.db.sql.Query
import io.zeko.db.sql.QueryBlock
import io.zeko.db.sql.aggregations.countGe
import io.zeko.db.sql.aggregations.sumGe
import io.zeko.db.sql.dsl.and
import io.zeko.db.sql.dsl.greaterEq
import io.zeko.db.sql.operators.eq
import io.zeko.db.sql.operators.inList
import org.springframework.stereotype.Component

interface QueryBuilder {
    fun build(request: CheckRequest, limits: List<LimitEntity>): String
}

@Component
class QueryBuilderImpl : QueryBuilder {


    override fun build(request: CheckRequest, limits: List<LimitEntity>): String {
        val schema = BuilderUtils.buildSchema(request)

        val queries = limits.flatMap { limit ->
            limit.criteria.map { criterion ->

                val timeFilter = BuilderUtils.buildTimeFilter(criterion)
                val staticFilter = BuilderUtils.buildStaticFilter(limit)
                val currencyFilter = BuilderUtils.buildCurrencyFilter(limit)
                val dynamicFilter = BuilderUtils.buildDynamicFilter(limit, schema)
                val aggregateFilter = BuilderUtils.buildAggregateFilter(limit, criterion)

                Query()
                    .from("transactions")
                    .fields(
                        "${BuilderUtils.quoted(limit.uuid)}::uuid as limitUuid",
                        "${BuilderUtils.quoted(criterion.uuid)}::uuid as criterionId"
                    )
                    .where(currencyFilter, staticFilter, dynamicFilter, timeFilter)
                    .having(aggregateFilter)
                    .toSql(shouldLineBreak = true)
            }
        }

        return queries.toList().joinToString("\nUNION ALL\n", transform = { s -> "($s)" })
    }

}

object BuilderUtils {
    data class Schema(val value: String, val sqlType: String)

    fun buildTimeFilter(criterion: Common.Criterion): QueryBlock {
        return "processedAt" greaterEq "(NOW() - INTERVAL ${quoted(criterion.interval)})"
    }

    fun buildCurrencyFilter(limit: LimitEntity): QueryBlock {
        return inList("currency", limit.currencies.toList(), true)
    }

    fun buildStaticFilter(limit: LimitEntity): QueryBlock {
        return (limit.staticFilter.map { filter -> inList(filter.property, filter.values.toList(), true) }
            .reduce { x, y -> x and y })
    }

    fun buildDynamicFilter(
        limit: LimitEntity,
        schema: Map<String, BuilderUtils.Schema>
    ): QueryBlock {
        return limit.dynamicFilter.map { property ->
            eq(
                property,
                schema.getValue(property).value,
                true
            )
        }.reduce { x, y -> x and y }
    }

    fun buildAggregateFilter(
        limit: LimitEntity,
        criterion: Common.Criterion
    ): QueryBlock {
        val aggregateFilter = if (limit.limitOn.aggregate == Common.Aggregate.Count)
            countGe(limit.limitOn.property, criterion.limit)
        else
            sumGe(limit.limitOn.property, criterion.limit)
        return aggregateFilter
    }

    fun buildSchema(request: CheckRequest): Map<String, Schema> {
        return mapOf(
            "terminal" to Schema(quoted(request.terminal), "uuid"),
            "amount" to Schema(request.amount.toString(), "numeric"),
            "currency" to Schema(quoted(request.currency), "varchar"),
            "cardHash" to Schema(quoted(request.cardHash), "varchar"),
            "mcc" to Schema(quoted(request.mcc), "varchar"),
            "gateway" to Schema(quoted(request.gateway), "varchar")
        )
    }


    fun quoted(value: Any): String {
        return "'$value'"
    }

}

