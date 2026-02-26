package com.ecommercestore.authservice.advices;

import com.ecommercestore.authservice.dtos.ErrorResponseDto;
import com.ecommercestore.authservice.exceptions.InvalidPasswordException;
import com.ecommercestore.authservice.exceptions.InvalidSessionException;
import com.ecommercestore.authservice.exceptions.UserCannotBeCreatedException;
import com.ecommercestore.authservice.exceptions.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ControllerAdvices {

    @ExceptionHandler(UserCannotBeCreatedException.class)
    private ResponseEntity<ErrorResponseDto> handleUserCannotBeCreatedException(UserCannotBeCreatedException e) {
        return new ResponseEntity<>(
                new ErrorResponseDto(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY),
                HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(UserNotFoundException.class)
    private ResponseEntity<ErrorResponseDto> handleUserNotFoundException(UserNotFoundException e) {
        return new ResponseEntity<>(
                new ErrorResponseDto(e.getMessage(), HttpStatus.NOT_FOUND),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidSessionException.class)
    private ResponseEntity<ErrorResponseDto> handleInvalidSessionException(InvalidSessionException e) {
        return new ResponseEntity<>(
                new ErrorResponseDto(e.getMessage(), HttpStatus.UNAUTHORIZED),
                HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(InvalidPasswordException.class)
    private ResponseEntity<ErrorResponseDto> handleInvalidPasswordException(InvalidPasswordException e) {
        return new ResponseEntity<>(
                new ErrorResponseDto(e.getMessage(), HttpStatus.UNAUTHORIZED),
                HttpStatus.UNAUTHORIZED);
    }
}
