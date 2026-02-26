package com.ecommercestore.authservice.dtos;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
@Setter
public class ErrorResponseDto {
    private String message;
    private HttpStatus statusCode;

    public ErrorResponseDto(String message, HttpStatus statusCode) {
        this.message = message;
        this.statusCode = statusCode;
    }
}
