package com.tomohavvk.limit

import arrow.core.Either
import com.tomohavvk.limit.error.AppError
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.web.WebProperties
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean

@SpringBootApplication
class Application

typealias AppFlow<T> = Either<AppError, T>

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}

@Bean
fun webResources(): WebProperties.Resources {
    return WebProperties.Resources()
}