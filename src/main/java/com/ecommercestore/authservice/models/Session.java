package com.ecommercestore.authservice.models;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;

@Setter
@Getter
@Entity
public class Session extends BaseModel {
    private String sessionToken;

    private LocalDateTime expiringAt;

    @ManyToOne
    private User user;

    @Enumerated(EnumType.ORDINAL)
    private SessionStatus sessionStatus;
}
