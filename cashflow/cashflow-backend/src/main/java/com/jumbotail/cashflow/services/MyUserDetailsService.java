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

import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public GetTransactionsResponse getTransactions(String userName, Long entityId) {
        GetTransactionsResponse getTransactionsResponse = userRepositoryService.getTransactions(userName, entityId);
        return getTransactionsResponse;
    }

    @Override
    public AddTransactionResponse addTransaction(AddTransactionRequest addTransactionRequest, String userName) {
        AddTransactionResponse addTransactionResponse = userRepositoryService.addTransaction(addTransactionRequest, userName);
        return addTransactionResponse;
    }

    @Override
    public EntityDto getEntity(String userName, Long entityId) {
        EntityDto entityDto = userRepositoryService.getEntity(userName, entityId);
        return entityDto;
    }

    @Override
    public void updateEntity(String userName, EntityDto entity) {
        userRepositoryService.updateEntity(userName, entity);
    }

    @Override
    public TransactionDto getTransaction(String userName, Long txnId, Long entityId) {
        return userRepositoryService.getTransaction(userName, txnId, entityId);
    }

    @Override
    public void updateTransaction(String userName, TransactionDto txn) {
        userRepositoryService.updateTransaction(userName, txn);
    }

    @Override
    public MonthlyAnalyticsResponse getMonthlyAnalytics(String userName, MonthlyAnalyticsRequest monthlyAnalyticsRequest) {
        GetTransactionsResponse getTransactionsResponse = userRepositoryService.getTransactions(userName);
        List<TransactionDto> transactionDtoList= getTransactionsResponse.getTransactionDtoList();
        Map<Integer, Long> monthlyCashIn = new HashMap<Integer, Long>();
        Map<Integer, Long> monthlyCashOut = new HashMap<Integer, Long>();

        Timestamp fromTime = monthlyAnalyticsRequest.getFromTimestamp();
        Timestamp toTime = monthlyAnalyticsRequest.getToTimestamp();

        for(TransactionDto transactionDto : transactionDtoList) {
            Timestamp time = transactionDto.getTimestamp();
            if(time != null && time.after(fromTime) && time.before(toTime)) {
                int month = getMonthFromTimeStamp(time);
                Long amount = transactionDto.getAmount();
                if(amount < 0) {
                    monthlyCashOut.put(month, monthlyCashOut.getOrDefault(month, Long.valueOf("0")) + amount);
                }
                else {
                    monthlyCashIn.put(month, monthlyCashIn.getOrDefault(month, Long.valueOf("0")) + amount);
                }
            }
        }

        MonthlyAnalyticsResponse monthlyAnalyticsResponse = new MonthlyAnalyticsResponse(monthlyCashIn, monthlyCashOut);

        return monthlyAnalyticsResponse;
    }

    private int getMonthFromTimeStamp(Timestamp timestamp) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp.getTime());
        return calendar.get(Calendar.MONTH);
    }

    private boolean userNameExistsAlready (String userName) {
        return userRepositoryService.userUserNameExists(userName);
    }

    private boolean userEmailExistsAlready (String email) {
        return userRepositoryService.userEmailExists(email);
    }
}
