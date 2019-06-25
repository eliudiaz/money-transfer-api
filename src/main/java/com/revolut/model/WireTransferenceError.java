package com.revolut.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class WireTransferenceError {
    private final String description;
    private final String causeCode;
}
