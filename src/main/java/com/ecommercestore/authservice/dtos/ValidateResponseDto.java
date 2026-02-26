package com.ecommercestore.authservice.dtos;

import com.ecommercestore.authservice.models.SessionStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ValidateResponseDto {
    UserDto userDto;
    SessionStatus sessionStatus;
}
