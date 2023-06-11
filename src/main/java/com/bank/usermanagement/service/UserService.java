package com.bank.usermanagement.service;

import com.bank.usermanagement.security.user.User;
import com.bank.usermanagement.security.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User getUserByEmail(String userName) {
        Optional<User> byEmail = userRepository.findByEmail(userName);
        return byEmail.orElseThrow(() -> new UsernameNotFoundException("User does not exist by email: " + userName));
    }
}
