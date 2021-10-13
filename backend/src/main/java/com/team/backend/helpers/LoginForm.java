package com.team.backend.helpers;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
public class LoginForm {

    @Size(min = 5, max = 100, message = "Adres email powinien zawierać od 5 do 100 znaków!")
    @NotBlank(message = "Adres email jest obowiązkowy!")
    @Pattern(regexp = "^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$",
            message = "Adres email powinien mieć następujący format - example@gmail.com!")
    private String email;
    @NotBlank(message = "Hasło jest obowiązkowe!")
    private String password;
}