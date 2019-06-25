package com.revolut.model;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.joda.money.Money;

import java.util.Date;
import java.util.List;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Account {

    private Long id;
    private String firstName;
    private String lastName;
    private Money balance;
    private Money previousBalance;
    private Date createdAt;
    private Date lastUpdate;
    private boolean enabled;
    private DisableReason disabledReason;
    private List<TransactionLog> recentTransactions;

}
