package com.jumbotail.cashflow.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EntityDto {
    @NotNull
    private Long id;
    @NotBlank
    private String type;
    @NotBlank
    private String name;
    private String contactNo;
    private String address;
}
