package com.revolut.model;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.joda.money.Money;

import java.util.Date;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WireTransference {
    private Long id;
    private Account origin;
    private Account target;
    private Long originAccountId;
    private Long targetAccountId;
    private Money amount;
    private Date createdAt;
    private Status status;
    private WireTransferenceError error;


    public enum Status {
        COMPLETED, FAILED
    }


}
