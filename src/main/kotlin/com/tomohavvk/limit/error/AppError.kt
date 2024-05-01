package com.tomohavvk.limit.error

sealed interface AppError {

    data class ValidationError(val reason: String) : AppError
    data class SomeAnotherError(val reason: String) : AppError

}