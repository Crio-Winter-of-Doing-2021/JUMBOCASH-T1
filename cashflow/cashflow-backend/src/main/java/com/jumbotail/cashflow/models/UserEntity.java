package com.jumbotail.cashflow.models;

import com.jumbotail.cashflow.dto.Entity;
import com.jumbotail.cashflow.dto.Transaction;
import lombok.Data;
import lombok.Generated;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

import net.bytebuddy.dynamic.loading.InjectionClassLoader;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

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

    private List<Entity> entitiesList;

    private List<Transaction> transactionsList;

    private Long cashIn;

    private Long cashOut;

}
