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

    @NotBlank(message = "Password is mandatory!")
    private String password;
    @NotBlank(message = "Old password is mandatory!")
    private String oldPassword;
}
