package com.jumbotail.cashflow.controller;

import com.jumbotail.cashflow.exchanges.AuthenticationRequest;
import com.jumbotail.cashflow.exchanges.AuthenticationResponse;
import com.jumbotail.cashflow.exchanges.UserRegistrationRequest;
import com.jumbotail.cashflow.exchanges.UserRegistrationResponse;
import com.jumbotail.cashflow.services.MyUserDetailsService;
import com.jumbotail.cashflow.services.UserService;
import com.jumbotail.cashflow.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class CashflowController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtTokenUtil;

    @Autowired
    private MyUserDetailsService userDetailsService;

    @GetMapping ("/hello")
    public ResponseEntity<String> hello() {
        return ResponseEntity.ok("Hello World");
    }

    @PostMapping("/authenticate")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword())
            );
        }
        catch (BadCredentialsException e) {
            throw new Exception("Incorrect username or password", e);
        }


        final UserDetails userDetails = userDetailsService
                .loadUserByUsername(authenticationRequest.getUsername());

        final String jwt = jwtTokenUtil.generateToken(userDetails);

        return ResponseEntity.ok(new AuthenticationResponse(jwt));
    }

    @PostMapping("/register")
    ResponseEntity<?> registerUser(@RequestBody UserRegistrationRequest userRegistrationRequest) throws Exception {

        UserRegistrationResponse userRegistrationResponse = userService.registerUser(userRegistrationRequest);

        return ResponseEntity.ok(userRegistrationResponse);
    }


}
