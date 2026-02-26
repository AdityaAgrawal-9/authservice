package com.ecommercestore.authservice.controllers;

import com.ecommercestore.authservice.dtos.LoginRequestDto;
import com.ecommercestore.authservice.dtos.UserDto;
import com.ecommercestore.authservice.dtos.ValidateResponseDto;
import com.ecommercestore.authservice.exceptions.InvalidPasswordException;
import com.ecommercestore.authservice.exceptions.InvalidSessionException;
import com.ecommercestore.authservice.exceptions.UserNotFoundException;
import com.ecommercestore.authservice.services.IAuthService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private IAuthService authService;

    public AuthController(@Qualifier("localAuthService") IAuthService authService) {
        this.authService = authService;
    }


    @PostMapping("/login")
    public ResponseEntity<UserDto> login(@RequestBody LoginRequestDto loginRequestDto)
            throws UserNotFoundException, InvalidPasswordException {
        return authService.loginUser(loginRequestDto.getEmail(), loginRequestDto.getPassword());
    }

    @PostMapping("/logout/{id}")
    public ResponseEntity<?> logout(@PathVariable("id") Long id,
                                    @RequestHeader("AUTH_TOKEN") String token)
            throws UserNotFoundException, InvalidSessionException {
        return authService.logoutUser(id, token);
    }


    @PostMapping("/validate")
    public ResponseEntity<ValidateResponseDto> validate(@RequestBody UserDto userDto,
                                                        @RequestHeader("AUTH_TOKEN") String token)
            throws UserNotFoundException, InvalidSessionException {
        return authService.validateUser(userDto.toUser(), token);
    }

}
