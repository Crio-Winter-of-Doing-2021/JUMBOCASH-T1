package com.jumbotail.cashflow.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionDto {
    @NotNull
    private Long id;
    @NotNull
    private Long amount;
    private String type;
    private String modeOfPayment;
    @NotNull
    private Timestamp timestamp;
    private Long entityId;
    private String remarks;
    private String status;
}
