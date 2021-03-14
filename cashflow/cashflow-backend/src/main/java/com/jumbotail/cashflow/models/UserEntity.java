package com.jumbotail.cashflow.models;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "cashflowusers")
@NoArgsConstructor
public class UserEntity {

    @Id
    private String id;

    @NotNull
    private String username;

    @NotNull
    private String password;

    @NotNull
    private String email;

    @NotNull
    private String roles;

    private boolean isActive;

}
