package com.jumbotail.cashflow.services;

import com.jumbotail.cashflow.dto.MyUserDetails;
import com.jumbotail.cashflow.dto.Transaction;
import com.jumbotail.cashflow.exchanges.*;
import com.jumbotail.cashflow.models.UserEntity;
import com.jumbotail.cashflow.repository.UserRepository;
import com.jumbotail.cashflow.repositoryServices.UserRepositoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.jumbotail.cashflow.dto.Entity;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Service
public class MyUserDetailsService implements UserDetailsService, UserService {

    @Autowired
    private UserRepositoryService userRepositoryService;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        MyUserDetails myUserDetails = userRepositoryService.getUserByUserName(userName);
        return myUserDetails;
    }

    @Override
    public UserRegistrationResponse registerUser(UserRegistrationRequest userRegistrationRequest) {

        String message = "";

        if(userNameExistsAlready(userRegistrationRequest.getUsername())) {
            message += "User Name already exists with username :" + userRegistrationRequest.getUsername();
        }
        if (userEmailExistsAlready(userRegistrationRequest.getEmail())) {
            message += "User Email already exists with Email :" + userRegistrationRequest.getEmail();
        }

        if(!message.isEmpty()) {
            UserRegistrationResponse userRegistrationResponse = new UserRegistrationResponse(userRegistrationRequest.getUsername(), userRegistrationRequest.getEmail(), userRegistrationRequest.getRoles(), message);
            return userRegistrationResponse;
        }

        UserRegistrationResponse userRegistrationResponse = userRepositoryService.createUser(userRegistrationRequest);
        userRegistrationResponse.setMessage("USER_REGISTRATION_SUCCESSFUL");

        return userRegistrationResponse;
    }

    @Override
    public AddEntityResponse addEntity(Entity entity, String email) {
        if(userEmailExistsAlready(email)) {
            String message = userRepositoryService.addEntity(entity, email);
            return new AddEntityResponse(message);
        }
        else {
            return new AddEntityResponse("User Email Not Found");
        }
    }

    @Override
    public GetEntitiesResponse getEntities(String email) {
        List<Entity> entityList = userRepositoryService.getEntities(email);
        GetEntitiesResponse getEntitiesResponse = new GetEntitiesResponse(entityList);
        return getEntitiesResponse;
    }

    @Override
    public GetTransactionsResponse getTransactions(String email) {
        GetTransactionsResponse getTransactionsResponse = userRepositoryService.getTransactions(email);
        return getTransactionsResponse;
    }

    @Override
    public AddTransactionResponse addTransaction(Transaction transaction, String email) {
        AddTransactionResponse addTransactionResponse = userRepositoryService.addTransaction(transaction, email);
        return addTransactionResponse;
    }

    private boolean userNameExistsAlready (String userName) {

        // if userName is not already present in db exception will be thrown;
        try{
            userRepositoryService.getUserByUserName(userName);
        }
        catch (UsernameNotFoundException e) {
            return false;
        }
        return true;
    }

    private boolean userEmailExistsAlready (String email) {

        // if email is not already present in db exception will be thrown;
        try{
            userRepositoryService.getUserByEmail(email);
        }
        catch (UsernameNotFoundException e) {
            return false;
        }
        return true;
    }
}
