package com.jumbotail.cashflow.exchanges;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRegistrationRequest {
    private String username;
    private String password;
    private String email;
    private String roles;
}
