package com.ecommercestore.authservice.controllers;

import com.ecommercestore.authservice.dtos.SignupUserRequestDto;
import com.ecommercestore.authservice.dtos.SignupUserResponseDto;
import com.ecommercestore.authservice.exceptions.UserCannotBeCreatedException;
import com.ecommercestore.authservice.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {
    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    public ResponseEntity<SignupUserResponseDto> signup(@RequestBody SignupUserRequestDto signupUserRequestDto)
            throws UserCannotBeCreatedException {
        try {
            Optional<SignupUserResponseDto> newUserDto = userService.signupUser(signupUserRequestDto);
            if(newUserDto.isEmpty()){
                throw new UserCannotBeCreatedException("User cannot be created");
            }
            return new ResponseEntity<>((newUserDto.get()), HttpStatus.CREATED);
        } catch (Exception e) {
            throw new UserCannotBeCreatedException("User cannot be created: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUser(@PathVariable("id") Long userId) {
        Optional<SignupUserResponseDto> user = userService.getUserById(userId);
        if (user.isEmpty()) {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(user.get(), HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> updateUser(@RequestBody SignupUserRequestDto signupUserRequestDto,
                                        @PathVariable("id") Long userId) {
        try {
            Optional<SignupUserResponseDto> updatedUser = userService.updateUser(userId, signupUserRequestDto);
            if (updatedUser.isEmpty()) {
                return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(updatedUser.get(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("User cannot be updated: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable("id") Long userId) {
        try {
            boolean isDeleted = userService.deleteUser(userId);
            if (!isDeleted) {
                return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>("User deleted successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("User cannot be deleted: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


}
