package com.example.CalendarManagement.Exception;

import com.example.CalendarManagement.DTO.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ApiResponse<String> handleIllegalArgumentException(IllegalArgumentException ex) {
        return new ApiResponse<>(
                "Invalid input data",
                400,
                null,
                getErrorDetails(ex.getMessage())
        );
    }

    @ExceptionHandler(DuplicateEmailException.class)
    public ApiResponse<String> handleDuplicateEmailException(DuplicateEmailException ex) {
        return new ApiResponse<>(
                "Invalid input data",
                400,
                null,
                getErrorDetails(ex.getMessage())
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiResponse<String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        String message = "Validation failed";
        return new ApiResponse<>(
                "Invalid input data",
                400,
                null,
                getErrorDetails(message)
        );
    }

    @ExceptionHandler(EmployeeNotFoundException.class)
    public ResponseEntity<ApiResponse<String>> handleEmployeeNotFoundException(EmployeeNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiResponse<>(ex.getMessage(), 404, "Error", null));
    }

    private Map<String, String> getErrorDetails(String message) {
        Map<String, String> errorDetails = new HashMap<>();
        errorDetails.put("details", message);
        return errorDetails;
    }
}
