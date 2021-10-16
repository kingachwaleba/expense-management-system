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
    @Size(min = 5, max = 45, message = "{user.login.size}")
    @NotBlank(message = "{user.login.notBlank}")
    @Pattern(regexp = "^(?=.*[A-Za-z0-9]$)[A-Za-z][A-Za-z\\d.-]{4,45}$",
            message = "{user.login.regexp}")
    private String login;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(nullable = false, length = 100)
    @Size(min = 5, max = 100, message = "{user.email.size}")
    @NotBlank(message = "{user.email.notBlank}")
    @Pattern(regexp = "^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$",
            message = "{user.email.regexp}")
    private String email;

    @Column
    @Size(max = 255)
    private String image;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(nullable = false)
    @NotBlank(message = "{user.password.notBlank}")
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