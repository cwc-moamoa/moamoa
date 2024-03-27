package com.teamsparta.moamoa.exception

import com.teamsparta.moamoa.exception.dto.ErrorResponseDto
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {
    private val logger: Logger = LoggerFactory.getLogger(GlobalExceptionHandler::class.java)

    @ExceptionHandler(IllegalStateException::class)
    fun handlerIllegalStateException(e: IllegalStateException): ResponseEntity<ErrorResponseDto> {
        logger.error("IllegalStateException 발생", e)
        return ResponseEntity
            .status(HttpStatus.CONFLICT)
            .body(ErrorResponseDto(e.message))
    }

    @ExceptionHandler(ModelNotFoundException::class)
    fun handleModeNotFoundException(e: ModelNotFoundException): ResponseEntity<ErrorResponseDto> {
        logger.error("ModelNotFoundException 발생", e)
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(ErrorResponseDto(e.message))
    }

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgumentException(e: IllegalArgumentException): ResponseEntity<ErrorResponseDto> {
        logger.error("IllegalArgumentException 발생", e)
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(ErrorResponseDto(e.message),)
    }

    @ExceptionHandler(InvalidCredentialException::class)
    fun handleInvalidCredentialException(e: InvalidCredentialException): ResponseEntity<ErrorResponseDto> {
        logger.error("InvalidCredentialException 발생", e)
        return ResponseEntity
            .status(HttpStatus.UNAUTHORIZED)
            .body(ErrorResponseDto(e.message,),
        )
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleMethodArgumentNotValidException(e: MethodArgumentNotValidException): ResponseEntity<ErrorResponseDto> {
        logger.error("MethodArgumentNotValidException 발생", e)
        val errors = e.bindingResult.fieldErrors.joinToString(separator = ", ") { "${it.field}: ${it.defaultMessage}" }
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(ErrorResponseDto(errors)) // (e.message)보다 이렇게 하는게 더 스웨거에서 깔끔하더라
    }
}
