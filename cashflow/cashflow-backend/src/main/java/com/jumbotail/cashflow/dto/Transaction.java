package com.jumbotail.cashflow.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {
    @NotNull
    private Long amount;
    private String type;
    private String modeOfPayment;
    @NotNull
    private Date timestamp;
    private Entity entity;
    private String remarks;
}
