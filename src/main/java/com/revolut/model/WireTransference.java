package com.revolut.model;


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
public class WireTransference {
    private Long id;
    private Account origin;
    private Account target;
    private Money amount;
    private Date createdAt;
    private Status status;
    private WireTransferenceError error;


    public enum Status {
        CREATED, IN_PROGRESS, COMPLETED, FAILED
    }


}
