package com.jumbotail.cashflow.controller;

import com.jumbotail.cashflow.dto.EntityDto;
import com.jumbotail.cashflow.dto.TransactionDto;
import com.jumbotail.cashflow.exchanges.*;
import com.jumbotail.cashflow.services.MyUserDetailsService;
import com.jumbotail.cashflow.services.UserService;
import com.jumbotail.cashflow.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
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

    private String getUserName() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        String username = userDetails.getUsername();
        return username;
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


    @PostMapping("/entity")
    ResponseEntity<?> addEntity(@RequestBody @Valid AddEntityRequest addEntityRequest ) {

        AddEntityResponse addEntityResponse = userService.addEntity(addEntityRequest, getUserName());

        return ResponseEntity.ok(addEntityResponse);
    }

    @GetMapping("/entity")
    ResponseEntity<?> getEntities( ) {

        GetEntitiesResponse getEntitiesResponse = userService.getEntities(getUserName());

        return ResponseEntity.ok(getEntitiesResponse);
    }

    @GetMapping("/transaction")
    ResponseEntity<?> getTransactions( ) {

        GetTransactionsResponse getTransactionsResponse = userService.getTransactions(getUserName());

        return ResponseEntity.ok(getTransactionsResponse);
    }

    @PostMapping("/transaction")
    ResponseEntity<?> addTransaction(@RequestBody @Valid AddTransactionRequest addTransactionRequest) {

        AddTransactionResponse addTransactionResponse = userService.addTransaction(addTransactionRequest, getUserName());

        return ResponseEntity.ok(addTransactionResponse);
    }



}
