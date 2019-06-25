package com.revolut.api.resources.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@AllArgsConstructor
@Getter
@Setter
public class WireTransferenceResultDto {
    private boolean success;
    private Long wireTransferenceId;
}
