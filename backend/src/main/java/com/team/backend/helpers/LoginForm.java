package com.team.backend.helpers;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class LoginForm {

    @NotBlank(message = "Login jest obowiązkowy!")
    private String email;

    @NotBlank(message = "Hasło jest obowiązkowe!")
    private String password;
}