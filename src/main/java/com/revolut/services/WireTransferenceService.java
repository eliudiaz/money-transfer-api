package com.revolut.services;

import com.revolut.api.resources.dto.requests.WireTransferenceRequestDto;
import com.revolut.api.resources.dto.responses.WireTransferenceResultDto;

public interface WireTransferenceService {

    WireTransferenceResultDto transfer(WireTransferenceRequestDto transference);

}
