package com.tomohavvk.limit.error

sealed interface AppError {
    val reason: String
}

data class ValidationError(override val reason: String) : AppError
data class DatabaseError(override val reason: String) : AppError
data class NotUniqueError(override val reason: String) : AppError