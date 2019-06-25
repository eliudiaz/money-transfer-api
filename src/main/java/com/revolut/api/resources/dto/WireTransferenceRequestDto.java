package com.revolut.api.resources.dto;

import com.revolut.model.Account;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WireTransferenceRequestDto {
    private Long originAccountId;
    private Long targetAccountId;
    private Account origin;
    private Account target;
    private BigDecimal amount;
}
