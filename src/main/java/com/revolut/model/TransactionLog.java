package com.revolut.model;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.joda.money.Money;

import java.util.Date;

@Builder
@Getter
@Setter
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TransactionLog {
    private Long id;
    private Account account;
    private Money amount;
    private Date createdAt;
    private Type type;


    public enum Type {
        DEBIT, CREDIT
    }


}
