package com.jumbotail.cashflow.exchanges;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MonthlyAnalyticsResponse {
    Map<Integer, Long> monthlyCashIn;
    Map<Integer, Long> monthlyCashOut;
}
