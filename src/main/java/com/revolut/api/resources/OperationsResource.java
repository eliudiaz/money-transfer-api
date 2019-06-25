package com.revolut.api.resources;


import com.revolut.api.resources.dto.WireTransferenceRequestDto;
import com.revolut.api.resources.dto.WireTransferenceResultDto;
import com.revolut.model.Account;
import com.revolut.services.AccountsService;
import com.revolut.services.WireTransferenceService;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/operations")
public class OperationsResource {

    private final WireTransferenceService transferenceService;
    private final AccountsService accountsService;

    @Inject
    public OperationsResource(final WireTransferenceService transferenceService,
                              final AccountsService accountsService) {
        this.transferenceService = transferenceService;
        this.accountsService = accountsService;
    }

    @Path("/transfer")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public WireTransferenceResultDto transfer(final WireTransferenceRequestDto wireTransferenceRequestDto) {
        final Account originAccount = accountsService.findById(wireTransferenceRequestDto.getOriginAccountId());
        final Account targetAccount = accountsService.findById(wireTransferenceRequestDto.getTargetAccountId());
        wireTransferenceRequestDto.setOrigin(originAccount);
        wireTransferenceRequestDto.setTarget(targetAccount);
        return this.transferenceService.transfer(wireTransferenceRequestDto);
    }

}
