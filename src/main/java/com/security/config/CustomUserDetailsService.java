package com.security.config;

import java.util.Collections;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.security.controller.AuthController;
import com.security.entity.User;
import com.security.repository.UserRepository;


@Service
public class CustomUserDetailsService implements UserDetailsService {
	private static final Logger logger = LogManager.getLogger(AuthController.class);

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        logger.info("CustomUserDetailsService || loadUserByUsername || called for: {}", usernameOrEmail);

        User user = userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail);
        if (user == null) {
            logger.info("User not found with username or email: {}", usernameOrEmail);
            throw new UsernameNotFoundException("User not found with username or email: " + usernameOrEmail);
        }

        logger.info("User found: {}", user.getUsername());
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), Collections.emptyList());
    }

}

