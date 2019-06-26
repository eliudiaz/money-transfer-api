package com.revolut.services;

import com.revolut.api.resources.dto.requests.WireTransferenceRequestDto;
import com.revolut.api.resources.dto.responses.WireTransferenceResultDto;
import com.revolut.model.WireTransference;

import java.util.List;

public interface WireTransferenceService {

    WireTransferenceResultDto transfer(WireTransferenceRequestDto transference);

    List<WireTransference> findAll();

}
