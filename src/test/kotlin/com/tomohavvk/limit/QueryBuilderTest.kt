package com.tomohavvk.limit

import arrow.core.NonEmptySet
import com.tomohavvk.limit.persistence.QueryBuilderImpl
import com.tomohavvk.limit.persistence.entity.Limit
import com.tomohavvk.limit.persistence.entity.LimitEntity
import com.tomohavvk.limit.protocol.Common.*
import com.tomohavvk.limit.protocol.Common.Currency
import com.tomohavvk.limit.protocol.request.CheckRequest
import org.junit.jupiter.api.Test
import java.util.*


class QueryBuilderTest {
    private val queryBuilder = QueryBuilderImpl()

    @Test
    fun `should correct build query`() {
        val expected = """
                          (SELECT 'dc19b4d8-1cea-43b6-b097-7d85baf5768b'::uuid as limitUuid, '11cecfec-c1af-4708-9a6b-c376a1535bdb'::uuid as criterionId FROM transactions WHERE
                          currency IN ('UAH') AND
                          mcc IN ('6012') AND
                          amount = 100 AND
                          processedAt >= (NOW() - INTERVAL '30 days') HAVING
                          SUM( amount ) >= 10)
                          UNION ALL
                          (SELECT 'dc19b4d8-1cea-43b6-b097-7d85baf5768b'::uuid as limitUuid, 'b114ffb1-1474-4fa4-a286-c7d08890b602'::uuid as criterionId FROM transactions WHERE
                          currency IN ('UAH') AND
                          mcc IN ('6012') AND
                          amount = 100 AND
                          processedAt >= (NOW() - INTERVAL '30 days') HAVING
                          SUM( amount ) >= 100)
        """.trimIndent()

        val actual = queryBuilder.build(checkRequest, listOf(limit)).trimIndent()

        assert(expected == actual)
    }

    private val checkRequest = CheckRequest(
        UUID.fromString("311d114c-4190-463e-9e75-edddf5366c0e"),
        UUID.fromString("1f828b92-63ec-47ef-b8bc-13e4c9c5f33a"),
        100,
        "USD",
        "someCardHash",
        "someMCC",
        "someGateway",
        System.currentTimeMillis()
    )
    private val limit = LimitEntity(
        uuid = UUID.fromString("dc19b4d8-1cea-43b6-b097-7d85baf5768b"),
        limit = Limit(
            name = "limit_one",
            description = "First limit",
            currency = Currency(
                property = "currency",
                values = NonEmptySet("UAH", setOf())
            ),
            limitOn = LimitOn(
                property = "amount",
                aggregate = Aggregate.Sum
            ),
            groupBy = NonEmptySet("amount", setOf()),
            filterBy = NonEmptySet(
                Filter(
                    property = "mcc",
                    values = NonEmptySet("6012", setOf())
                ), setOf()
            ),
            criteria = NonEmptySet(
                Criterion(
                    uuid = UUID.fromString("11cecfec-c1af-4708-9a6b-c376a1535bdb"),
                    limit = 10,
                    interval = 30
                ),
                setOf(
                    Criterion(
                        uuid = UUID.fromString("b114ffb1-1474-4fa4-a286-c7d08890b602"),
                        limit = 100,
                        interval = 30
                    )
                )
            )
        )
    )

}

