package com.firefly.slumbus.base.exception.handler;

import com.firefly.slumbus.base.code.ErrorCode;
import com.firefly.slumbus.base.dto.ErrorResponseDTO;
import com.firefly.slumbus.base.exception.BadRequestException;
import com.firefly.slumbus.base.exception.ConflictException;
import com.firefly.slumbus.base.exception.InvalidValueException;
import com.firefly.slumbus.base.exception.UserNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CustomExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(CustomExceptionHandler.class);

    @ExceptionHandler(UserNotFoundException.class)
    protected ResponseEntity<ErrorResponseDTO> handleUserNotFoundException(final UserNotFoundException e) {
        log.error("handleUserNotFoundException: {}", e.getMessage());
        return ResponseEntity
                .status(ErrorCode.USER_NOT_FOUND.getStatus().value())
                .body(new ErrorResponseDTO(ErrorCode.USER_NOT_FOUND));
    }

    @ExceptionHandler(BadRequestException.class)
    protected ResponseEntity<ErrorResponseDTO> handleBadRequestException(final BadRequestException e) {
        log.error("handleBadRequestException: {}", e.getMessage());
        return ResponseEntity
                .status(e.getErrorCode().getStatus().value())
                .body(new ErrorResponseDTO(e.getErrorCode()));
    }

    @ExceptionHandler(ConflictException.class)
    protected ResponseEntity<ErrorResponseDTO> handleConflictException(final ConflictException e) {
        log.error("handleConflictException: {}", e.getMessage());
        return ResponseEntity
                .status(e.getErrorCode().getStatus().value())
                .body(new ErrorResponseDTO(e.getErrorCode()));
    }

    @ExceptionHandler(InvalidValueException.class)
    protected ResponseEntity<ErrorResponseDTO> handleInvalidValueException(final InvalidValueException e) {
        log.error("handleInvalidValueException: {}", e.getMessage());
        return ResponseEntity
                .status(e.getErrorCode().getStatus().value())
                .body(new ErrorResponseDTO(e.getErrorCode()));
    }
}
