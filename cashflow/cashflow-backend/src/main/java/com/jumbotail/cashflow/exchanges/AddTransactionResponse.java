package com.jumbotail.cashflow.exchanges;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddTransactionResponse {
    private Long cashIn;
    private Long cashOut;
}
