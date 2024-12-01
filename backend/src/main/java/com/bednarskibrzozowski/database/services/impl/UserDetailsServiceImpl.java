package com.bednarskibrzozowski.database.services.impl;

import com.bednarskibrzozowski.database.domain.entities.UserEntity;
import com.bednarskibrzozowski.database.repositories.UserRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        UserEntity userEntity = userRepository.findById(username).get();

        return User
                .withUsername(userEntity.getUsername())
                .password(userEntity.getPassword())
                .build();

    }
}
