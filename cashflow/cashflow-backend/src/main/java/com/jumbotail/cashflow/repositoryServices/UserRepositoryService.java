package com.jumbotail.cashflow.repositoryServices;

import com.jumbotail.cashflow.dto.EntityDto;
import com.jumbotail.cashflow.dto.MyUserDetails;
import com.jumbotail.cashflow.dto.TransactionDto;
import com.jumbotail.cashflow.exchanges.*;

import java.util.List;


public interface UserRepositoryService {

    MyUserDetails getUserByUserName(String userName);

    Boolean userEmailExists(String email);

    Boolean userUserNameExists(String userName);

    UserRegistrationResponse createUser(UserRegistrationRequest userRegistrationRequest);

    String addEntity(AddEntityRequest addEntityRequest, String userName);

    List<EntityDto> getEntities(String userName);

    GetTransactionsResponse getTransactions(String userName);
    
    GetTransactionsResponse getTransactions(String userName, Long entityId);

    AddTransactionResponse addTransaction(AddTransactionRequest addTransactionRequest, String userName);

    EntityDto getEntity(String userName, Long entityId);

    void updateEntity(String userName, EntityDto entity);

    TransactionDto getTransaction(String userName, Long txnId, Long entityId);

    void updateTransaction(String userName, TransactionDto txn);
}
