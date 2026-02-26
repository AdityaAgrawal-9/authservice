package com.ecommercestore.authservice.dtos;

import com.ecommercestore.authservice.models.Role;
import com.ecommercestore.authservice.models.User;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SignupUserRequestDto {
    private String name;
    private String email;
    private String password;
    private List<Role> roles;

    public static SignupUserRequestDto fromUser(User user) {
        SignupUserRequestDto signupUserRequestDto = new SignupUserRequestDto();
        signupUserRequestDto.setName(user.getName());
        signupUserRequestDto.setEmail(user.getEmail());
//        signupUserRequestDto.setPassword(user.getPassword());  Do not return the password in the DTO
        signupUserRequestDto.setRoles(user.getRoles());
        return signupUserRequestDto;
    }

    public User toUser(){
        User user = new User();
        user.setName(this.getName());
        user.setEmail(this.getEmail());
        user.setPassword(this.getPassword());
        user.setRoles(this.getRoles());
        return user;
    }



}
