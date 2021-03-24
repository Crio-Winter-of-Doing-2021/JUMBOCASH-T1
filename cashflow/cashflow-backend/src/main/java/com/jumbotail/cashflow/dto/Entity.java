package com.jumbotail.cashflow.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Entity {
    @NotBlank
    @NotNull
    private String type;
    @NotBlank
    @NotNull
    private String name;
    private String contactNo;
    private String address;
}
