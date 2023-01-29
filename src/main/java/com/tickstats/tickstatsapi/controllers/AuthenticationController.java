package com.tickstats.tickstatsapi.controllers;


import com.tickstats.tickstatsapi.requestresponse.UsernamePasswordRequest;
import com.tickstats.tickstatsapi.requestresponse.JwtResponse;
import com.tickstats.tickstatsapi.security.JwtUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import com.tickstats.tickstatsapi.utils.JwtUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import static com.tickstats.tickstatsapi.security.WebSecurityConfig.HTTPS_ENABLED;
import static com.tickstats.tickstatsapi.utils.JwtUtils.JWT_TOKEN_VALIDITY;


@RestController
@CrossOrigin(origins = {"${access.control.allow.origin}"}, allowCredentials = "true")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtTokenUtil;

    @Autowired
    private JwtUserDetailsService userDetailsService;

    @PostMapping( "/authenticate")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody UsernamePasswordRequest authenticationRequest, HttpServletResponse response) throws Exception {

        authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());//throw exception if not valid



        final UserDetails userDetails = userDetailsService
                .loadUserByUsername(authenticationRequest.getUsername());


        final String token = jwtTokenUtil.generateToken(userDetails);

        Cookie cookie = new Cookie("jwttoken", token);

        // expires in 7 days
        cookie.setMaxAge((int) JWT_TOKEN_VALIDITY);

        // optional properties
        if(HTTPS_ENABLED) {
            cookie.setSecure(true);
        }
        cookie.setHttpOnly(true);
        cookie.setPath("/");

        // add cookie to response
        response.addCookie(cookie);

        return ResponseEntity.ok(new JwtResponse(token));
    }

    private void authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            System.out.println("USER_DISABLED");
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            System.out.println("INVALID_CREDENTIALS");
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }
}