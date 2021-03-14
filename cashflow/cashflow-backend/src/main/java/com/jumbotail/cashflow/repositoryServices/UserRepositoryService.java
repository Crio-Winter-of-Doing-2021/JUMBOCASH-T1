package com.jumbotail.cashflow.repositoryServices;

import com.jumbotail.cashflow.dto.MyUserDetails;
import com.jumbotail.cashflow.exchanges.UserRegistrationRequest;
import com.jumbotail.cashflow.exchanges.UserRegistrationResponse;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserRepositoryService {

    MyUserDetails getUserByUserName(String userName);

    MyUserDetails getUserByEmail(String email);

    UserRegistrationResponse createUser(UserRegistrationRequest userRegistrationRequest);
}
