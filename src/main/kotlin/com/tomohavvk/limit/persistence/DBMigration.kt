package com.tomohavvk.limit.persistence

import org.springframework.boot.autoconfigure.flyway.FlywayProperties
import org.springframework.boot.autoconfigure.r2dbc.R2dbcProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(FlywayProperties::class)
class DBMigration {

    @Bean(initMethod = "migrate")
    fun flyway(flywayProperties: FlywayProperties, r2dbcProperties: R2dbcProperties): org.flywaydb.core.Flyway {
        return org.flywaydb.core.Flyway.configure()
            .dataSource(flywayProperties.url, r2dbcProperties.username, r2dbcProperties.password)
            .locations(*flywayProperties.locations.toArray { arrayOf<String>() }).baselineOnMigrate(true)
            .load()
    }
}