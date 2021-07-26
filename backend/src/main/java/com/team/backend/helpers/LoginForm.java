package com.team.backend.helpers;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class LoginForm {

    @NotBlank
    private String login;

    @NotBlank
    private String password;
}