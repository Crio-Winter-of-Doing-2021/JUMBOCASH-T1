package com.jumbotail.cashflow.exchanges;

import com.jumbotail.cashflow.dto.EntityDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetEntitiesResponse {
    private List<EntityDto> entityDtoList;
}
