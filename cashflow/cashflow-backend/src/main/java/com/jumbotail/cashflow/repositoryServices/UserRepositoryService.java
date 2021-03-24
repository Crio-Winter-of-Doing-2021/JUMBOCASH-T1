package com.jumbotail.cashflow.repositoryServices;

import com.jumbotail.cashflow.dto.Entity;
import com.jumbotail.cashflow.dto.MyUserDetails;
import com.jumbotail.cashflow.dto.Transaction;
import com.jumbotail.cashflow.exchanges.AddTransactionResponse;
import com.jumbotail.cashflow.exchanges.GetTransactionsResponse;
import com.jumbotail.cashflow.exchanges.UserRegistrationRequest;
import com.jumbotail.cashflow.exchanges.UserRegistrationResponse;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;


public interface UserRepositoryService {

    MyUserDetails getUserByUserName(String userName);

    MyUserDetails getUserByEmail(String email);

    UserRegistrationResponse createUser(UserRegistrationRequest userRegistrationRequest);

    String addEntity(Entity entity, String email);

    List<Entity> getEntities(String email);

    GetTransactionsResponse getTransactions(String email);

    AddTransactionResponse addTransaction(Transaction transaction, String email);
}
