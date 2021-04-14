package com.jumbotail.cashflow.services;

import com.jumbotail.cashflow.dto.EntityDto;
import com.jumbotail.cashflow.dto.TransactionDto;
import com.jumbotail.cashflow.exchanges.*;


public interface UserService {
    UserRegistrationResponse registerUser(UserRegistrationRequest userRegistrationRequest);

    AddEntityResponse addEntity(AddEntityRequest addEntityRequest, String userName);

    GetEntitiesResponse getEntities(String userName);

    GetTransactionsResponse getTransactions(String userName);

    GetTransactionsResponse getTransactions(String userName, Long entityId);

    AddTransactionResponse addTransaction(AddTransactionRequest addTransactionRequest, String userName);

    EntityDto getEntity(String userName, Long entityId);

    void updateEntity(String userName, EntityDto entity);

    TransactionDto getTransaction(String userName, Long txnId, Long entityId);

    void updateTransaction(String userName, TransactionDto txn);
}
