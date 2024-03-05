package com.teamsparta.moamoa.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler(OutOfStockException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleOutOfStockException(e: OutOfStockException): ErrorResponse {
        return ErrorResponse("OutOfStock", e.message ?: "Out of stock")
    }
}
