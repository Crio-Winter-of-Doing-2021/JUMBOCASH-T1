package com.jumbotail.cashflow.repository;

import com.jumbotail.cashflow.models.UserEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<UserEntity, String> {

}
