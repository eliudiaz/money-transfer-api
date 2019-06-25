package com.revolut.services;

import com.revolut.api.resources.dto.WireTransferenceRequestDto;
import com.revolut.api.resources.dto.WireTransferenceResultDto;

public interface WireTransferenceService {

    WireTransferenceResultDto transfer(WireTransferenceRequestDto transference);

}
