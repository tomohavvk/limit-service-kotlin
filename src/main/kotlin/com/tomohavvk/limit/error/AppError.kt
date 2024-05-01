package com.tomohavvk.limit.error

sealed interface AppError {
    val reason: String
}

data class ValidationError(override val reason: String) : AppError
data class SomeAnotherError(override val reason: String) : AppError