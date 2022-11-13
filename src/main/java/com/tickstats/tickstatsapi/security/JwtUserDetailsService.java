package com.tickstats.tickstatsapi.security;

import java.util.ArrayList;

import com.tickstats.tickstatsapi.repositories.UserRepository;
import com.tickstats.tickstatsapi.repositories.entities.MyUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        MyUser user = userRepository.findByUsername(username);
        if (user == null)
            throw new UsernameNotFoundException(username);

        //test passsword: "$2a$10$w4nKP8r4UfFzAN/Eu8LKEeOITeEQnaHAGax0Ew0FPHip5WA/1dIAS"

        return new User( user.getUsername(), user.getPassword(), new ArrayList<>());//spring security user
    }
}