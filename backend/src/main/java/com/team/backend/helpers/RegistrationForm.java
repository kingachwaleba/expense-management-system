package com.team.backend.helpers;

import com.team.backend.model.User;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class RegistrationForm {

    @Valid
    private User user;
    @NotBlank(message = "Potwierdzenie hasła jest obowiązkowe!")
    private String confirmPassword;
}
