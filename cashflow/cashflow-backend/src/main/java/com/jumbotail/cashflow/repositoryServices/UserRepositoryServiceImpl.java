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
    public GetTransactionsResponse getTransactions(String userName, Long entityId) {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STANDARD);

        UserEntity userEntity = userRepository.findByUserName(userName).get();

        Long cashIn = Long.valueOf("0");
        Long cashOut = Long.valueOf("0");
        List<TransactionDto> transactionDtoList = new ArrayList<>();

        List<TransactionEntity> transactionEntities = userEntity.getTransactionEntities();

        for(TransactionEntity txnEntity : transactionEntities) {
            if(txnEntity.getId() == entityId) {
                List<Transaction> txnList = txnEntity.getTransactions();
                for(Transaction txn : txnList) {
                    TransactionDto transactionDto = modelMapper.map(txn, TransactionDto.class);
                    transactionDto.setEntityId(txnEntity.getId());
                    Long amount = transactionDto.getAmount();
                    if(amount < 0) {
                        cashOut += amount;
                    }
                    else {
                        cashIn += amount;
                    }
                    transactionDtoList.add(transactionDto);
                }
            }
        }
        Collections.sort(transactionDtoList, (a, b) -> (b.getTimestamp().compareTo(a.getTimestamp())));
        GetTransactionsResponse getTransactionsResponse = new GetTransactionsResponse(transactionDtoList, cashIn, cashOut);
        return getTransactionsResponse;
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
        Collections.sort(allTransactionDtoList, (a, b) -> (b.getTimestamp().compareTo(a.getTimestamp())));
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

    @Override
    public EntityDto getEntity(String userName, Long entityId) {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STANDARD);

        UserEntity userEntity = userRepository.findByUserName(userName).get();

        List<TransactionEntity> transactionEntities = userEntity.getTransactionEntities();

        for(TransactionEntity txnEntity : transactionEntities) {
            if (txnEntity.getId() == entityId) {
                EntityDto entityDto = modelMapper.map(txnEntity, EntityDto.class);
                return entityDto;
            }
        }
        return null;
    }

    @Override
    public void updateEntity(String userName, EntityDto entity) {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STANDARD);

        UserEntity userEntity = userRepository.findByUserName(userName).get();
        List<TransactionEntity> transactionEntities = userEntity.getTransactionEntities();
        int len = transactionEntities.size();
        for(int i = 0 ; i< len ; i++) {
            if (transactionEntities.get(i).getId() == entity.getId()) {
                TransactionEntity txnEntity = modelMapper.map(entity, TransactionEntity.class);
                txnEntity.setTransactions(transactionEntities.get(i).getTransactions());
                transactionEntities.set(i, txnEntity);
                break;
            }
        }
        userRepository.save(userEntity);
    }

    @Override
    public TransactionDto getTransaction(String userName, Long txnId, Long entityId) {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STANDARD);

        UserEntity userEntity = userRepository.findByUserName(userName).get();
        List<TransactionEntity> transactionEntities = userEntity.getTransactionEntities();
        for(TransactionEntity txnEntity : transactionEntities) {
            if(txnEntity.getId() == entityId) {
                List<Transaction> txns = txnEntity.getTransactions();
                for(Transaction txn : txns) {
                    if(txn.getId() == txnId) {
                        TransactionDto txnDto = modelMapper.map(txn, TransactionDto.class);
                        txnDto.setEntityId(entityId);
                        return txnDto;
                    }
                }
            }
        }
        return null;
    }

    @Override
    @Transactional
    public void updateTransaction(String userName, TransactionDto txn) {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STANDARD);

        UserEntity userEntity = userRepository.findByUserName(userName).get();
        List<TransactionEntity> transactionEntities = userEntity.getTransactionEntities();
        for(TransactionEntity txnEntity : transactionEntities) {
            if(txnEntity.getId() == txn.getEntityId()) {
                List<Transaction> txns = txnEntity.getTransactions();
                int len = txns.size();
                for(int i = 0 ; i< len ; i++) {
                    if(txns.get(i).getId() == txn.getId()) {
                        Transaction finalTxn = modelMapper.map(txn, Transaction.class);
                        if (finalTxn.getAmount() != txns.get(i).getAmount()) {
                            Long cashOut = userEntity.getCashOut();
                            Long cashIn = userEntity.getCashIn();
                            if(txns.get(i).getAmount() < 0 ) {
                                cashOut -= txns.get(i).getAmount();
                            }
                            else {
                                cashIn -= txns.get(i).getAmount();
                            }

                            if(finalTxn.getAmount() < 0) {
                                cashOut += finalTxn.getAmount();
                            }
                            else {
                                cashIn += finalTxn.getAmount();
                            }
                            userEntity.setCashOut(cashOut);
                            userEntity.setCashIn(cashIn);

                        }
                        txns.set(i, finalTxn);
                        break;
                    }
                }
            }
        }
        userRepository.save(userEntity);
    }


}
