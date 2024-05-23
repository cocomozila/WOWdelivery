package com.wow.delivery.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Entity
@Getter
@NoArgsConstructor
@Table(uniqueConstraints = { @UniqueConstraint(name = "email_phoneNumber_unique", columnNames = { "email", "phone_number" }) })
public class User extends BaseEntity {

    @Column(name = "email", columnDefinition = "VARCHAR(30)", nullable = false)
    private String email;

    @Column(name = "password", columnDefinition = "VARCHAR(100)", nullable = false)
    private String password;

    @Column(name = "salt", columnDefinition = "VARCHAR(100)", nullable = false)
    private String salt;

    @Column(name = "phone_number", columnDefinition = "VARCHAR(20)", nullable = false)
    private String phoneNumber;

    @Builder
    public User(String email, String password, String salt, String phoneNumber) {
        this.email = email;
        this.password = password;
        this.salt = salt;
        this.phoneNumber = phoneNumber;
    }
}
