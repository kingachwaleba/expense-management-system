package com.team.backend.service;

import com.team.backend.model.User;

public interface JavaMailService {

    void sendMessage(User user, String appUrl);
}
