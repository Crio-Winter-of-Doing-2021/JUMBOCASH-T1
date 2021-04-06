package com.jumbotail.cashflow.repositoryServices;

import com.jumbotail.cashflow.dto.EntityDto;
import com.jumbotail.cashflow.dto.TransactionDto;
import com.jumbotail.cashflow.dto.MyUserDetails;
import com.jumbotail.cashflow.exchanges.*;
import com.jumbotail.cashflow.models.Transaction;
import com.jumbotail.cashflow.models.TransactionEntity;
import com.jumbotail.cashflow.models.UserEntity;
import com.jumbotail.cashflow.repository.TransactionEntityRepository;
import com.jumbotail.cashflow.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class UserRepositoryServiceImpl implements UserRepositoryService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TransactionEntityRepository transactionEntityRepository;

    @Override
    public MyUserDetails getUserByUserName(String userName) {
        Optional<UserEntity> userEntity = userRepository.findByUserName(userName);
        if (!userEntity.isPresent()) {
            throw new UsernameNotFoundException("Not Found Any User with UserName: " + userName);
        }
        MyUserDetails myUserDetails = new MyUserDetails(userEntity.get());
        return myUserDetails;
    }

    @Override
    public Boolean userEmailExists(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public Boolean userUserNameExists(String userName) {
        return userRepository.existsByUserName(userName);
    }

    @Override
    public UserRegistrationResponse createUser(UserRegistrationRequest userRegistrationRequest) {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STANDARD);

        UserEntity userEntity = modelMapper.map(userRegistrationRequest, UserEntity.class);
        userEntity.setTransactionEntities(new ArrayList<>());
        userEntity.setCashIn(Long.valueOf("0"));
        userEntity.setCashOut(Long.valueOf("0"));
        userEntity = userRepository.save(userEntity);
        UserRegistrationResponse userRegistrationResponse = modelMapper.map(userEntity, UserRegistrationResponse.class);

        return userRegistrationResponse;
    }

    @Override
    public String addEntity(AddEntityRequest addEntityRequest, String userName) {

        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STANDARD);

        UserEntity userEntity = userRepository.findByUserName(userName).get();

        List<TransactionEntity> transactionEntities = userEntity.getTransactionEntities();
        TransactionEntity transactionEntity = modelMapper.map(addEntityRequest, TransactionEntity.class);
        transactionEntity.setTransactions(new ArrayList<>());
        transactionEntities.add(transactionEntity);
        userRepository.save(userEntity);

        return "Entity added Successfully";
    }

    @Override
    public List<EntityDto> getEntities(String userName) {

        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STANDARD);

        UserEntity userEntity = userRepository.findByUserName(userName).get();

        List<TransactionEntity> transactionEntities = userEntity.getTransactionEntities();
        List<EntityDto> entityDtoList = new ArrayList<>();

        for(TransactionEntity txnEntity : transactionEntities) {
            EntityDto entityDto = modelMapper.map(txnEntity, EntityDto.class);
            entityDtoList.add(entityDto);
        }

        return entityDtoList;
    }

    @Override
    public GetTransactionsResponse getTransactions(String userName) {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STANDARD);

        UserEntity userEntity = userRepository.findByUserName(userName).get();

        Long cashIn = userEntity.getCashIn();
        Long cashOut = userEntity.getCashOut();
        List<TransactionDto> allTransactionDtoList = new ArrayList<>();

        List<TransactionEntity> transactionEntities = userEntity.getTransactionEntities();

        for(TransactionEntity txnEntity : transactionEntities) {
            List<Transaction> txnList = txnEntity.getTransactions();
            for(Transaction txn : txnList) {
                TransactionDto transactionDto = modelMapper.map(txn, TransactionDto.class);
                transactionDto.setEntityId(txnEntity.getId());
                allTransactionDtoList.add(transactionDto);
            }
        }
        Collections.sort(allTransactionDtoList, (a, b) -> (a.getTimestamp().compareTo(b.getTimestamp())));
        GetTransactionsResponse getTransactionsResponse = new GetTransactionsResponse(allTransactionDtoList, cashIn, cashOut);
        return getTransactionsResponse;
    }

    @Override
    @Transactional
    public AddTransactionResponse addTransaction(AddTransactionRequest addTransactionRequest, String userName) {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STANDARD);

        UserEntity userEntity = userRepository.findByUserName(userName).get();

        List<TransactionEntity> txnEntities = userEntity.getTransactionEntities();
        TransactionEntity entity = null;
        for(TransactionEntity txnEntity : txnEntities) {
            if(txnEntity.getId() == addTransactionRequest.getEntityId()) {
                entity = txnEntity;
                break;
            }
        }
        if(entity == null) {
            return null;
        }

        Long amountToTransact = addTransactionRequest.getAmount();
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

        Transaction txn = modelMapper.map(addTransactionRequest, Transaction.class);
        entity.getTransactions().add(txn);

        userEntity = userRepository.save(userEntity);

        AddTransactionResponse addTransactionResponse = new AddTransactionResponse(userEntity.getCashIn(), userEntity.getCashOut());

        return addTransactionResponse;
    }


}
