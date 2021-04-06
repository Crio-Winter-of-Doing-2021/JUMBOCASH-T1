package com.jumbotail.cashflow.models;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "transactions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "amount", nullable = false)
    private Long amount;

    @Column(name = "type", nullable = false)
    private String type;

    @Column(name = "mode_of_payment", nullable = false)
    private String modeOfPayment;

    @Column(name = "timestamp", nullable = false)
    private Timestamp timestamp;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "remarks", nullable = false)
    private String remarks;

}
