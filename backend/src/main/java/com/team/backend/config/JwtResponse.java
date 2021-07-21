package com.team.backend.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Getter
@Setter
public class JwtResponse {

    private String token;
    private String type = "Bearer";
    private String login;
    private Collection<? extends GrantedAuthority> authorities;

    public JwtResponse(String accessToken, String login, Collection<? extends GrantedAuthority> authorities) {
        this.token = accessToken;
        this.login = login;
        this.authorities = authorities;
    }
}