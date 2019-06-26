package com.revolut.api.resources.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class AccountResponseDto {
    private Long id;
    private String firstName;
    private String lastName;
    private BigDecimal balance;
    private Date createdAt;
    private Date lastUpdate;
}
