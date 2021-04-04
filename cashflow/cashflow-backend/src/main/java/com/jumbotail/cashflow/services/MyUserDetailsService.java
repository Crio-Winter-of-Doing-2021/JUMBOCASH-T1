package com.jumbotail.cashflow.services;

import com.jumbotail.cashflow.dto.MyUserDetails;
import com.jumbotail.cashflow.dto.TransactionDto;
import com.jumbotail.cashflow.exchanges.*;
import com.jumbotail.cashflow.repositoryServices.UserRepositoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.jumbotail.cashflow.dto.EntityDto;

import java.util.List;

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

        if(userNameExistsAlready(userRegistrationRequest.getUserName())) {
            message += "User Name already exists with username :" + userRegistrationRequest.getUserName();
        }
        if (userEmailExistsAlready(userRegistrationRequest.getEmail())) {
            message += "User Email already exists with Email :" + userRegistrationRequest.getEmail();
        }

        if(!message.isEmpty()) {
            UserRegistrationResponse userRegistrationResponse = new UserRegistrationResponse(userRegistrationRequest.getUserName(), userRegistrationRequest.getEmail(), userRegistrationRequest.getRoles(), message);
            return userRegistrationResponse;
        }

        UserRegistrationResponse userRegistrationResponse = userRepositoryService.createUser(userRegistrationRequest);
        userRegistrationResponse.setMessage("USER_REGISTRATION_SUCCESSFUL");

        return userRegistrationResponse;
    }

    @Override
    public AddEntityResponse addEntity(AddEntityRequest addEntityRequest, String userName) {
        String message = userRepositoryService.addEntity(addEntityRequest, userName);
        AddEntityResponse addEntityResponse = new AddEntityResponse(message);
        return addEntityResponse;
    }

    @Override
    public GetEntitiesResponse getEntities(String userName) {
        List<EntityDto> entityDtoList = userRepositoryService.getEntities(userName);
        GetEntitiesResponse getEntitiesResponse = new GetEntitiesResponse(entityDtoList);
        return getEntitiesResponse;
    }

    @Override
    public GetTransactionsResponse getTransactions(String userName) {
        GetTransactionsResponse getTransactionsResponse = userRepositoryService.getTransactions(userName);
        return getTransactionsResponse;
    }

    @Override
    public AddTransactionResponse addTransaction(AddTransactionRequest addTransactionRequest, String userName) {
        AddTransactionResponse addTransactionResponse = userRepositoryService.addTransaction(addTransactionRequest, userName);
        return addTransactionResponse;
    }

    private boolean userNameExistsAlready (String userName) {
        return userRepositoryService.userUserNameExists(userName);
    }

    private boolean userEmailExistsAlready (String email) {
        return userRepositoryService.userEmailExists(email);
    }
}
