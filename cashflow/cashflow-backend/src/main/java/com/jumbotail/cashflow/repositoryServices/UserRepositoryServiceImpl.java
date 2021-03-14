package com.jumbotail.cashflow.repositoryServices;

import com.jumbotail.cashflow.dto.MyUserDetails;
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
        userEntity = mongoTemplate.save(userEntity);
        UserRegistrationResponse userRegistrationResponse = modelMapper.map(userEntity, UserRegistrationResponse.class);

        return userRegistrationResponse;
    }


}
