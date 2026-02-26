package com.ecommercestore.authservice.services;

import com.ecommercestore.authservice.dtos.UserDto;
import com.ecommercestore.authservice.dtos.ValidateResponseDto;
import com.ecommercestore.authservice.exceptions.InvalidPasswordException;
import com.ecommercestore.authservice.exceptions.InvalidSessionException;
import com.ecommercestore.authservice.exceptions.UserNotFoundException;
import com.ecommercestore.authservice.models.User;
import org.springframework.http.ResponseEntity;

public interface IAuthService {

    ResponseEntity<UserDto> loginUser(String email, String password) throws UserNotFoundException, InvalidPasswordException;
    ResponseEntity<ValidateResponseDto> validateUser(User user, String token) throws UserNotFoundException, InvalidSessionException;
    ResponseEntity<?> logoutUser(Long userId, String token) throws UserNotFoundException, InvalidSessionException;
}
