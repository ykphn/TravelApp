package com.example.travelapp.utility

sealed class ScreenState<T>(
    val data :T? =null,
    val message: String? = null
) {
    class Loading<T> : ScreenState<T>()
    class Success<T>(data: T) : ScreenState<T>(data)
    class Error<T>(message: String, data: T? = null) : ScreenState<T>(data, message)


}