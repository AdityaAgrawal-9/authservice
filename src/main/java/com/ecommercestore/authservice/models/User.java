package com.ecommercestore.authservice.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(uniqueConstraints = {@UniqueConstraint(name = "unique_email",columnNames = "email")})
public class User extends BaseModel {
    private String name;
    private String email;
    private String password;
    @ManyToMany
    private List<Role> roles;

    @OneToMany(mappedBy = "user")
    private List<Session> sessions;
}
