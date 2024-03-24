package com.teamsparta.moamoa.exception

import com.teamsparta.moamoa.exception.dto.ErrorResponseDto
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler(IllegalStateException::class)
    fun handlerIllegalStateException(e: IllegalStateException): ResponseEntity<ErrorResponseDto> {
        return ResponseEntity
            .status(HttpStatus.CONFLICT)
            .body(ErrorResponseDto(e.message))
    }

    @ExceptionHandler(ModelNotFoundException::class)
    fun handleModeNotFoundException(e: ModelNotFoundException): ResponseEntity<ErrorResponseDto> {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorResponseDto(e.message))
    }

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgumentException(e: IllegalArgumentException): ResponseEntity<ErrorResponseDto> {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
            ErrorResponseDto(
                e.message,
            ),
        )
    }

    @ExceptionHandler(InvalidCredentialException::class)
    fun handleInvalidCredentialException(e: InvalidCredentialException): ResponseEntity<ErrorResponseDto> {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
            ErrorResponseDto(
                e.message,
            ),
        )
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleMethodArgumentNotValidException(e: MethodArgumentNotValidException): ResponseEntity<ErrorResponseDto> {
        val errors = e.bindingResult.fieldErrors.joinToString(separator = ", ") { "${it.field}: ${it.defaultMessage}" }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorResponseDto(errors)) // (e.message)보다 이렇게 하는게 더 스웨거에서 깔끔하더라
    }
}
