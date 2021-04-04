package com.jumbotail.cashflow.exchanges;

import com.jumbotail.cashflow.dto.TransactionDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetTransactionsResponse {
    private List<TransactionDto> transactionDtoList;
    private Long cashIn;
    private Long cashOut;
}
