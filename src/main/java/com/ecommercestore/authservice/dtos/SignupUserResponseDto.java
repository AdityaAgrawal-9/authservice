package com.ecommercestore.authservice.dtos;

import com.ecommercestore.authservice.models.Role;
import com.ecommercestore.authservice.models.User;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SignupUserResponseDto {

    private Long Id;
    private String name;
    private String email;
    private List<Role> roles;

    public static SignupUserResponseDto fromUser(User user) {
    SignupUserResponseDto signupUserResponseDto = new SignupUserResponseDto();
        signupUserResponseDto.setId(user.getId());
        signupUserResponseDto.setName(user.getName());
        signupUserResponseDto.setEmail(user.getEmail());
//        signupUserResponseDto.setPassword(user.getPassword());  Do not return the password in the DTO
        signupUserResponseDto.setRoles(user.getRoles());
        return signupUserResponseDto;
    }

    public User toUser(){
        User user = new User();
        user.setId(this.getId());
        user.setName(this.getName());
        user.setEmail(this.getEmail());
        user.setRoles(this.getRoles());
        return user;
    }
}
