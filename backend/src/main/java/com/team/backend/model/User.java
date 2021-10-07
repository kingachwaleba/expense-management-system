package com.team.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(unique = true, nullable = false, length = 45)
    @Size(min = 5, max = 45)
    @NotBlank(message = "Login is mandatory!")
    @Pattern(regexp = "^(?=.*[A-Za-z0-9]$)[A-Za-z][A-Za-z\\d.-]{4,45}$",
            message = "Incorrect format of a login (it could contain letters, numbers, -, " +
                    "it should start with letter, it should have 5-45 characters)!")
    private String login;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(nullable = false, length = 100)
    @Size(min = 5, max = 100)
    @NotBlank(message = "Email is mandatory!")
    @Pattern(regexp = "^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$",
            message = "Incorrect format of an email (example@gmail.com)!")
    private String email;

    @Column
    @Size(max = 255)
    private String image;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(nullable = false)
    @Size(max = 255)
    private String password;

    @Column(length = 1)
    private String deleted;

    @Transient
    private String roles = "ROLE_USER";

    @JsonIgnore
    @Size(max = 255)
    @Column(name = "token")
    private String token;

    @JsonIgnore
    @Column(name = "expiry_date", columnDefinition = "DATETIME")
    private LocalDateTime expiryDate;

    public enum AccountType {
        Y, // yes - deleted account
        N // no - active account
    }
}