package com.team.backend.helpers;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
public class LoginForm {

    @Size(min = 5, max = 100, message = "{user.email.size}")
    @NotBlank(message = "{user.email.notBlank}")
    @Pattern(regexp = "^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$",
            message = "{user.email.regexp}")
    private String email;

    @NotBlank(message = "{user.password.notBlank}")
    private String password;
}