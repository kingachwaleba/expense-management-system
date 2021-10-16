package com.team.backend.config;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class JwtResponse {

    private String token;
    private Date expiryDate;
    private String type = "Bearer";
    private String login;
    private String image;

    public JwtResponse(String accessToken, Date expiryDate, String login, String image) {
        this.token = accessToken;
        this.expiryDate = expiryDate;
        this.login = login;
        this.image = image;
    }
}