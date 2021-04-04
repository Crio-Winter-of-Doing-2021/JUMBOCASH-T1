package com.jumbotail.cashflow.models;

import com.jumbotail.cashflow.dto.TransactionDto;
import com.jumbotail.cashflow.dto.EntityDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "entities")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "type", nullable = false)
    private String type;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "contact_no")
    private String contactNo;

    @Column(name = "address")
    private String address;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "transaction_fk", referencedColumnName = "id")
    List<Transaction> transactions = new ArrayList<>();
}
