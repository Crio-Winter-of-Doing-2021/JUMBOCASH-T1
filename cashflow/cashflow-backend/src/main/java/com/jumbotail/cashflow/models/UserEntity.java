package com.jumbotail.cashflow.models;

import com.jumbotail.cashflow.dto.TransactionDto;
import com.jumbotail.cashflow.dto.EntityDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "user_name", nullable = false)
    private String userName;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "roles", nullable = false)
    private String roles;

    @Column(name = "cash_in", nullable = false)
    private Long cashIn;

    @Column(name = "cash_out", nullable = false)
    private Long cashOut;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "entity_fk", referencedColumnName = "id")
    List<TransactionEntity> transactionEntities = new ArrayList<>();

}
