package com.jumbotail.cashflow.controller;

import com.jumbotail.cashflow.dto.Entity;
import com.jumbotail.cashflow.dto.Transaction;
import com.jumbotail.cashflow.exchanges.*;
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

import javax.validation.Valid;

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
    ResponseEntity<?> registerUser(@Valid @RequestBody UserRegistrationRequest userRegistrationRequest) throws Exception {

        UserRegistrationResponse userRegistrationResponse = userService.registerUser(userRegistrationRequest);

        return ResponseEntity.ok(userRegistrationResponse);
    }


    @PostMapping("/{email}/entity")
    ResponseEntity<?> addEntity(@RequestBody @Valid Entity entity , @PathVariable String email ) {

        AddEntityResponse addEntityResponse = userService.addEntity(entity, email);

        return ResponseEntity.ok(addEntityResponse);
    }

    @GetMapping("/{email}/entity")
    ResponseEntity<?> getEntities(@PathVariable String email ) {

        GetEntitiesResponse getEntitiesResponse = userService.getEntities(email);

        return ResponseEntity.ok(getEntitiesResponse);
    }

    @GetMapping("/{email}/transaction")
    ResponseEntity<?> getTransactions(@PathVariable String email ) {

        GetTransactionsResponse getTransactionsResponse = userService.getTransactions(email);

        return ResponseEntity.ok(getTransactionsResponse);
    }

    @PostMapping("/{email}/transaction")
    ResponseEntity<?> addTransaction(@RequestBody @Valid Transaction transaction , @PathVariable String email ) {

        AddTransactionResponse addTransactionResponse = userService.addTransaction(transaction, email);

        return ResponseEntity.ok(addTransactionResponse);
    }



}
