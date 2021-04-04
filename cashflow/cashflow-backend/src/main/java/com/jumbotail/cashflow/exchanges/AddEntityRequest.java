package com.jumbotail.cashflow.exchanges;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddEntityRequest {

    @NotBlank
    private String type;
    @NotBlank
    private String name;
    private String contactNo;
    private String address;
}
