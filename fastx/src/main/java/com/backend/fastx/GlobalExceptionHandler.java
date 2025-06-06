package com.backend.fastx;

import com.backend.fastx.exception.*;
import com.backend.fastx.exception.IllegalStateException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;


@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<?> handleDataIntegrityViolation(DataIntegrityViolationException e) {
        String message = "Duplicate data: " + extractDetail(e.getMessage());
        return buildErrorResponse(message, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(exception = GenderInvalidException.class)
    public ResponseEntity<?> handleGenderException(GenderInvalidException e){
        return buildErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(exception = InvalidInputException.class)
    public ResponseEntity<?> handleInputException(InvalidInputException e){
        return buildErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(exception = RoleInvalidException.class)
    public ResponseEntity<?> handleRoleException(RoleInvalidException e){
        return buildErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(exception = InvalidBusTypeException.class)
    public ResponseEntity<?> handleBusTypeException(InvalidBusTypeException e){
        return buildErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(exception = ResourceNotFoundException.class)
    public ResponseEntity<?> handleResourceNotFoundException(ResourceNotFoundException e){
        return buildErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidSeatTypeException.class)
    public ResponseEntity<?> handleSeatTypeException(InvalidSeatTypeException e){
        return buildErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(exception = UnauthorizedBusAccessException.class)
    public ResponseEntity<?> handleUnauthorizedBusAccessException(UnauthorizedBusAccessException e){
        return buildErrorResponse(e.getMessage(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(exception = IllegalStateException.class)
    public ResponseEntity<?> handleIllegalStateException(IllegalStateException e){
        return buildErrorResponse(e.getMessage(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(exception = UnauthorizedAccessException.class)
    public ResponseEntity<?> handleUnauthorizedAccessException(UnauthorizedAccessException e){
        return buildErrorResponse(e.getMessage(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(exception = UserAccessDeniedException.class)
    public ResponseEntity<?> handleUserAccessDeniedException(UserAccessDeniedException e){
        return buildErrorResponse(e.getMessage(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<?> handleJsonParseError(HttpMessageNotReadableException e) {
        Throwable cause = e.getCause();

        while (cause != null) {
            if (cause instanceof GenderInvalidException ||
                cause instanceof InvalidInputException ||
                cause instanceof RoleInvalidException ||
                cause instanceof InvalidBusTypeException ||
                cause instanceof ResourceNotFoundException ||
                cause instanceof InvalidSeatTypeException ||
                cause instanceof UnauthorizedBusAccessException ||
                cause instanceof UnauthorizedAccessException ||
                cause instanceof UserAccessDeniedException ||
                cause instanceof IllegalStateException)
            {
                return buildErrorResponse(cause.getMessage(), HttpStatus.BAD_REQUEST);
            }
            cause = cause.getCause();
        }
        return buildErrorResponse("Invalid request. Please check the input format.", HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGeneralErrors(Exception e) {
        return buildErrorResponse("An unexpected error occurred.", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // handling the duplicate entry for the email
    private String extractDetail(String fullMessage) {
        if (fullMessage.contains("Duplicate entry")) {
            try {
                int start = fullMessage.indexOf("Duplicate entry '") + "Duplicate entry '".length();
                int end = fullMessage.indexOf("'", start);
                String email = fullMessage.substring(start, end);
                return "Email '" + email + "' already exists, kindly login";
            } catch (Exception e) {
                return "Email already exists, kindly login";
            }
        }
        return "Data already exists.";
    }

    private ResponseEntity<Map<String, String>> buildErrorResponse(String message, HttpStatus status) {
        Map<String, String> map = new HashMap<>();
        map.put("message", message);
        return ResponseEntity.status(status).body(map);
    }

}
