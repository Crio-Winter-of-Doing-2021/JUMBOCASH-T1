package com.jumbotail.cashflow.exchanges;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRegistrationResponse {
    private String userName;
    private String email;
    private String roles;
    private String message;
}