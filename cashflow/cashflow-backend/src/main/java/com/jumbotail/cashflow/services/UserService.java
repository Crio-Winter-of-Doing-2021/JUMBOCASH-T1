package com.jumbotail.cashflow.services;

import com.jumbotail.cashflow.dto.EntityDto;
import com.jumbotail.cashflow.dto.TransactionDto;
import com.jumbotail.cashflow.exchanges.*;


public interface UserService {
    UserRegistrationResponse registerUser(UserRegistrationRequest userRegistrationRequest);

    AddEntityResponse addEntity(AddEntityRequest addEntityRequest, String userName);

    GetEntitiesResponse getEntities(String userName);

    GetTransactionsResponse getTransactions(String userName);

    AddTransactionResponse addTransaction(AddTransactionRequest addTransactionRequest, String userName);
}
