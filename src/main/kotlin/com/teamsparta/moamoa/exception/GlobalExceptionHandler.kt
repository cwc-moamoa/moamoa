package com.teamsparta.moamoa.exception


import org.springframework.http.HttpStatus
import com.teamsparta.moamoa.exception.dto.ErrorResponseDto
import org.springframework.http.ResponseEntity
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

    @ExceptionHandler(OutOfStockException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleOutOfStockException(e: OutOfStockException): ErrorResponseDto {
        return ErrorResponse("OutOfStock", e.message ?: "Out of stock")
    
    @ExceptionHandler(handleModeNotFoundException::class)
    fun handleModeNotFoundException(e: ModelNotFoundException): ResponseEntity<ErrorResponseDto> {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorResponse(e.message))
    }

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgumentException(e: IllegalArgumentException): ResponseEntity<ErrorResponseDto> {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorResponse(e.message))
    }

    @ExceptionHandler(InvalidCredentialException::class)
    fun handleInvalidCredentialException(e: InvalidCredentialException): ResponseEntity<ErrorResponseDto> {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ErrorResponse(e.message))
    }
}
