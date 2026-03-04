package com.ecommercestore.authservice.services;

import com.ecommercestore.authservice.dtos.SendEmailDto;
import com.ecommercestore.authservice.dtos.SignupUserRequestDto;
import com.ecommercestore.authservice.dtos.SignupUserResponseDto;
import com.ecommercestore.authservice.models.User;
import com.ecommercestore.authservice.repositories.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService implements IUserService {
    private UserRepository userRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    public UserService(UserRepository userRepository,
                       BCryptPasswordEncoder bCryptPasswordEncoder,
                       KafkaTemplate<String, String> kafkaTemplate) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public Optional<SignupUserResponseDto> signupUser(SignupUserRequestDto signupUserRequestDto) {
        User passedUser = signupUserRequestDto.toUser();
        passedUser.setPassword(bCryptPasswordEncoder.encode(passedUser.getPassword()));
        User createdUser = userRepository.save(passedUser);

        if(createdUser != null) {
            SendEmailDto sendEmailDto = new SendEmailDto();
//          Set the System FROM Email in the environment variable and fetch. Never HardCode username and passwords.
            sendEmailDto.setFrom(System.getenv("USER_NAME"));
            sendEmailDto.setTo(createdUser.getEmail());
            sendEmailDto.setSubject("User Signup Successful.");
            sendEmailDto.setBody("Hi " + createdUser.getName() + ",\n\n"+
                    "Welcome to the E-Commerce Store! \n" +
                    "You have been registered Successfuly.\n" +
                    "You can login and start buying available products now.\n" +
                    "Thank You & Happy Shopping !!!\n\n" +
                    "Regards,\n" +
                    "Aditya's E-Commerce Store\n.");


            try {
                kafkaTemplate.send(
                        "notification.send.email",
                        objectMapper.writeValueAsString(sendEmailDto)
                );
            }catch(Exception e){
                System.out.println("Error in sending email");
            }

        }

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
