package com.team.backend.helpers;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePasswordHolder {

    @NotBlank(message = "Hasło jest obowiązkowe!")
    private String password;
    @NotBlank(message = "Potwierdzenie hasła jest obowiązkowe!")
    private String confirmPassword;
    @NotBlank(message = "Stare hasło jest obowiązkowe!")
    private String oldPassword;
}
