package com.revolut.api.resources.dto.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreateAccountRequestDto {
    private String firstName;
    private String lastName;
    private BigDecimal initialBalance;
}
