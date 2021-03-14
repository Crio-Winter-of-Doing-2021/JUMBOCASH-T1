package com.jumbotail.cashflow.services;

import com.jumbotail.cashflow.dto.MyUserDetails;
import com.jumbotail.cashflow.exchanges.UserRegistrationRequest;
import com.jumbotail.cashflow.exchanges.UserRegistrationResponse;

public interface UserService {
    UserRegistrationResponse registerUser(UserRegistrationRequest userRegistrationRequest);
}
