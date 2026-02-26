package com.ecommercestore.authservice.dtos;

import com.ecommercestore.authservice.models.Role;
import com.ecommercestore.authservice.models.User;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserDto {
    private Long id;
    private String name;
    private String email;
    private List<Role> roles;

    public static UserDto fromUser(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setName(user.getName());
        userDto.setEmail(user.getEmail());
        userDto.setRoles(user.getRoles());

        return userDto;
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
