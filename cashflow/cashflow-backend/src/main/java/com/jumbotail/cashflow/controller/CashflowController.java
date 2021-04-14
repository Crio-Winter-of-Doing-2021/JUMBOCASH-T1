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
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.lang.reflect.Field;
import java.util.Map;

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
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserRegistrationRequest userRegistrationRequest) {

        UserRegistrationResponse userRegistrationResponse = userService.registerUser(userRegistrationRequest);

        return ResponseEntity.ok(userRegistrationResponse);
    }


    @PostMapping("/entity")
    ResponseEntity<?> addEntity(@RequestBody @Valid AddEntityRequest addEntityRequest ) {

        AddEntityResponse addEntityResponse = userService.addEntity(addEntityRequest, getUserName());

        return ResponseEntity.ok(addEntityResponse);
    }

    @GetMapping("/entity")
    public ResponseEntity<?> getEntities( ) {

        GetEntitiesResponse getEntitiesResponse = userService.getEntities(getUserName());

        return ResponseEntity.ok(getEntitiesResponse);
    }

    @GetMapping("/entity/{entityId}")
    public ResponseEntity<?> getEntities(@PathVariable Long entityId ) {

        EntityDto entity = userService.getEntity(getUserName(), entityId);

        return ResponseEntity.ok(entity);
    }

    @GetMapping("/transaction")
    public ResponseEntity<?> getTransactions( ) {

        GetTransactionsResponse getTransactionsResponse = userService.getTransactions(getUserName());

        return ResponseEntity.ok(getTransactionsResponse);
    }

    @GetMapping("/transaction/entity/{entityId}")
    public ResponseEntity<?> getTransactions( @PathVariable Long entityId) {

        GetTransactionsResponse getTransactionsResponse = userService.getTransactions(getUserName(), entityId);

        return ResponseEntity.ok(getTransactionsResponse);
    }

    @GetMapping("/transaction/{txnId}/entity/{entityId}")
    public ResponseEntity<?> getTransaction( @PathVariable Long txnId, @PathVariable Long entityId) {

        TransactionDto txn = userService.getTransaction(getUserName(), txnId, entityId);

        return ResponseEntity.ok(txn);
    }

    @PostMapping("/transaction")
    public ResponseEntity<?> addTransaction(@RequestBody @Valid AddTransactionRequest addTransactionRequest) {

        AddTransactionResponse addTransactionResponse = userService.addTransaction(addTransactionRequest, getUserName());

        return ResponseEntity.ok(addTransactionResponse);
    }

    @PatchMapping("/entity/{entityId}")
    public ResponseEntity<?> updateEntity(@PathVariable Long entityId, @RequestBody Map<Object, Object> fields) {
        EntityDto entity = userService.getEntity(getUserName(), entityId);
        fields.forEach((k, v) -> {
            Field field = ReflectionUtils.findField(EntityDto.class, (String) k);
            field.setAccessible(true);
            ReflectionUtils.setField(field, entity, v);
        });
        userService.updateEntity(getUserName(), entity);
        return ResponseEntity.ok("Successful");
    }

    @PatchMapping("/transaction/{txnId}/entity/{entityId}")
    public ResponseEntity<?> updateEntity(@PathVariable(name="txnId",required=true) Long txnId, @PathVariable(name="entityId",required=true) Long entityId, @RequestBody Map<Object, Object> fields) {
        TransactionDto txn = userService.getTransaction(getUserName(), txnId, entityId);
        fields.forEach((k, v) -> {
            Field field = ReflectionUtils.findField(TransactionDto.class, (String) k);
            field.setAccessible(true);
            if(k.equals("amount") || k.equals("entityId")) {
                ReflectionUtils.setField(field, txn, Long.valueOf("" + v));
            }
            else {
                ReflectionUtils.setField(field, txn, v);
            }
        });
        userService.updateTransaction(getUserName(), txn);
        return ResponseEntity.ok("Successful");
    }





}
