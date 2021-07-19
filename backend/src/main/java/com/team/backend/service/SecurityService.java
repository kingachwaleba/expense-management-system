package com.team.backend.service;

public interface SecurityService {

    boolean isAuthenticated();
    void autoLogin(String login, String password);
}
