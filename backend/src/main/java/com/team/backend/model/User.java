package com.team.backend.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(unique = true, nullable = false, length = 45)
    @Size(min = 5, max = 45)
    @NotBlank(message = "Login is mandatory!")
    private String login;

    @Column(unique = true, nullable = false, length = 100)
    @Size(min = 5, max = 100)
    @NotBlank(message = "Email is mandatory!")
    private String email;

    @Column
    @Size(max = 255)
    private String image;

    @Column(nullable = false)
    @Size(max = 255)
    private String password_hash;

    @Column(nullable = false, length = 45)
    @Size(max = 45)
    private String salt;

    @Column(length = 1)
    private String deleted;
}