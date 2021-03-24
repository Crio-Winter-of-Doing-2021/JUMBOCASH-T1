package com.jumbotail.cashflow.repositoryServices;

import com.jumbotail.cashflow.dto.Entity;
import com.jumbotail.cashflow.dto.MyUserDetails;
import com.jumbotail.cashflow.dto.Transaction;
import com.jumbotail.cashflow.exchanges.AddTransactionResponse;
import com.jumbotail.cashflow.exchanges.GetTransactionsResponse;
import com.jumbotail.cashflow.exchanges.UserRegistrationRequest;
import com.jumbotail.cashflow.exchanges.UserRegistrationResponse;
import com.jumbotail.cashflow.models.UserEntity;
import org.modelmapper.ModelMapper;
import org.modelmapper.Provider;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Service
public class UserRepositoryServiceImpl implements UserRepositoryService {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public MyUserDetails getUserByUserName(String userName) {
        UserEntity userEntity = mongoTemplate.findOne(query(where("username").is(userName)), UserEntity.class);
        if (userEntity == null) {
            throw new UsernameNotFoundException("Not Found Any User with UserName: " + userName);
        }
        MyUserDetails myUserDetails = new MyUserDetails(userEntity);
        return myUserDetails;
    }

    @Override
    public MyUserDetails getUserByEmail(String email) {
        UserEntity userEntity = mongoTemplate.findOne(query(where("email").is(email)), UserEntity.class);
        if (userEntity == null) {
            throw new UsernameNotFoundException("Not Found Any User with Email: " + email);
        }
        MyUserDetails myUserDetails = new MyUserDetails(userEntity);
        return myUserDetails;
    }

    @Override
    public UserRegistrationResponse createUser(UserRegistrationRequest userRegistrationRequest) {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STANDARD);

        UserEntity userEntity = modelMapper.map(userRegistrationRequest, UserEntity.class);
        userEntity.setActive(true);
        userEntity.setEntitiesList(new LinkedList<Entity>());
        userEntity.setTransactionsList(new LinkedList<Transaction>());
        userEntity.setCashIn(Long.valueOf("0"));
        userEntity.setCashOut(Long.valueOf("0"));
        userEntity = mongoTemplate.save(userEntity);
        UserRegistrationResponse userRegistrationResponse = modelMapper.map(userEntity, UserRegistrationResponse.class);

        return userRegistrationResponse;
    }

    @Override
    public String addEntity(Entity entity, String email) {

        UserEntity userEntity = mongoTemplate.findOne(query(where("email").is(email)), UserEntity.class);

        List<Entity> entityList = userEntity.getEntitiesList();
        entityList.add(entity);
        mongoTemplate.save(userEntity);

        return "Entity added Successfully";
    }

    @Override
    public List<Entity> getEntities(String email) {
        UserEntity userEntity = mongoTemplate.findOne(query(where("email").is(email)), UserEntity.class);

        List<Entity> entityList = userEntity.getEntitiesList();

        return entityList;
    }

    @Override
    public GetTransactionsResponse getTransactions(String email) {
        UserEntity userEntity = mongoTemplate.findOne(query(where("email").is(email)), UserEntity.class);

        List<Transaction> transactionList = userEntity.getTransactionsList();
        Long cashIn = userEntity.getCashIn();
        Long cashOut = userEntity.getCashOut();

        GetTransactionsResponse getTransactionsResponse = new GetTransactionsResponse(transactionList, cashIn, cashOut);
        return getTransactionsResponse;
    }

    @Override
    public AddTransactionResponse addTransaction(Transaction transaction, String email) {
        UserEntity userEntity = mongoTemplate.findOne(query(where("email").is(email)), UserEntity.class);
        List<Transaction> transactionList = userEntity.getTransactionsList();
        transactionList.add(transaction);
        Long amountToTransact = transaction.getAmount();
        Long cashIn = userEntity.getCashIn();
        Long cashOut = userEntity.getCashOut();
        if(amountToTransact > 0 ) {
            cashIn = cashIn + amountToTransact;
            userEntity.setCashIn(cashIn);
        }
        else{
            cashOut = cashOut + amountToTransact;
            userEntity.setCashOut(cashOut);
        }
        userEntity = mongoTemplate.save(userEntity);

        AddTransactionResponse addTransactionResponse = new AddTransactionResponse(userEntity.getCashIn(), userEntity.getCashOut());

        return addTransactionResponse;
    }


}
