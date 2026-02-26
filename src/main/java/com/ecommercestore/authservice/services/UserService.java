package com.ecommercestore.authservice.services;

import com.ecommercestore.authservice.dtos.SignupUserRequestDto;
import com.ecommercestore.authservice.dtos.SignupUserResponseDto;
import com.ecommercestore.authservice.models.User;
import com.ecommercestore.authservice.repositories.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService implements IUserService {
    private UserRepository userRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public Optional<SignupUserResponseDto> signupUser(SignupUserRequestDto signupUserRequestDto) {
        User passedUser = signupUserRequestDto.toUser();
        passedUser.setPassword(bCryptPasswordEncoder.encode(passedUser.getPassword()));
        User createdUser = userRepository.save(passedUser);
        return Optional.of(SignupUserResponseDto.fromUser(createdUser));
    }

    public Optional<SignupUserResponseDto> getUserById(Long userId) {
        Optional<User> user = userRepository.findById(userId);
//        if() {
//            return Optional.empty();
//        }
        if(user.isEmpty() || (user.isPresent() && user.get().getIsDeleted() == true)){
            return Optional.empty();
        }
        return Optional.of(SignupUserResponseDto.fromUser(user.get()));
    }

    public Optional<SignupUserResponseDto> updateUser(Long userId, SignupUserRequestDto updateUserDto) {
        Optional<User> existingUser = userRepository.findById(userId);
        if(existingUser.isEmpty() || (existingUser.isPresent() && existingUser.get().getIsDeleted() == true)){
            return Optional.empty();
        }
        User user = existingUser.get();
        if (updateUserDto.getName() != null) {
            user.setName(updateUserDto.getName());
        }
        if (updateUserDto.getEmail() != null) {
            user.setEmail(updateUserDto.getEmail());
        }
        if (updateUserDto.getPassword() != null) {
            user.setPassword(updateUserDto.getPassword());
        }
        if (updateUserDto.getRoles() != null) {
            user.setRoles(updateUserDto.getRoles());
        }
        User updatedUser = userRepository.save(user);
        return Optional.of(SignupUserResponseDto.fromUser(updatedUser));
    }

    public boolean deleteUser(Long userId) {
        Optional<User> existingUser = userRepository.findById(userId);
        if(existingUser.isEmpty() || (existingUser.isPresent() && existingUser.get().getIsDeleted() == true)){
            return false;
        }
        User user = existingUser.get();
        user.setIsDeleted(true);
        userRepository.save(user);
        return true;
    }
}
