package com.jumbotail.cashflow.services;

import com.jumbotail.cashflow.dto.Entity;
import com.jumbotail.cashflow.dto.MyUserDetails;
import com.jumbotail.cashflow.dto.Transaction;
import com.jumbotail.cashflow.exchanges.*;


public interface UserService {
    UserRegistrationResponse registerUser(UserRegistrationRequest userRegistrationRequest);

    AddEntityResponse addEntity(Entity entity, String email);

    GetEntitiesResponse getEntities(String email);

    GetTransactionsResponse getTransactions(String email);

    AddTransactionResponse addTransaction(Transaction transaction, String email);
}
