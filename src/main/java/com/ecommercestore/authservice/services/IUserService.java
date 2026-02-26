package com.ecommercestore.authservice.services;

import com.ecommercestore.authservice.dtos.SignupUserRequestDto;
import com.ecommercestore.authservice.dtos.SignupUserResponseDto;

import java.util.Optional;

public interface IUserService {
    Optional<SignupUserResponseDto> signupUser(SignupUserRequestDto user);
}
