package com.team.backend.service;

import com.team.backend.exception.UserNotFoundException;
import com.team.backend.model.User;
import com.team.backend.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String s) {
        User user = userRepository.findByEmail(s).orElseThrow(UserNotFoundException::new);

        return UserPrinciple.build(user);
    }
}
