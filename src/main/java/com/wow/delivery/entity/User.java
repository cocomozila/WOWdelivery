package com.wow.delivery.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@Table(uniqueConstraints = { @UniqueConstraint(name = "email_phoneNumber_unique", columnNames = { "email", "phoneNumber" }) })
public class User extends BaseEntity {

    @Column(name = "email", columnDefinition = "VARCHAR(30)", nullable = false)
    private String email;

    @Column(name = "password", columnDefinition = "VARCHAR(100)", nullable = false)
    private String password;

    @Column(name = "salt", columnDefinition = "VARCHAR(100)", nullable = false)
    private String salt;

    @Column(name = "phoneNumber", columnDefinition = "VARCHAR(20)", nullable = false)
    private String phoneNumber;

    @OneToMany(mappedBy = "user")
    private List<Address> addresses = new ArrayList<>();

    @Builder
    public User(String email, String password, String salt, String phoneNumber) {
        this.email = email;
        this.password = password;
        this.salt = salt;
        this.phoneNumber = phoneNumber;
    }
}
