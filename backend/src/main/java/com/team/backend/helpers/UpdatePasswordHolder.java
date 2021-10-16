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

    @NotBlank(message = "{user.password.notBlank}")
    private String password;

    @NotBlank(message = "{user.confirmPassword.notBlank}")
    private String confirmPassword;

    @NotBlank(message = "{user.oldPassword.notBlank}")
    private String oldPassword;
}
