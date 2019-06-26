package com.revolut.api.resources;


import com.revolut.api.resources.dto.requests.AccountDto;
import com.revolut.api.resources.dto.requests.DisableRequestDto;
import com.revolut.api.resources.dto.responses.AccountResponseDto;
import com.revolut.api.resources.dto.responses.SearchResultDto;
import com.revolut.model.Account;
import com.revolut.services.AccountsService;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/accounts")
public class AccountsResource {

    private final AccountsService accountsService;

    @Inject
    public AccountsResource(final AccountsService accountsService) {
        this.accountsService = accountsService;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public SearchResultDto<Account> all() {
        final List<Account> list = accountsService.findAll();
        return new SearchResultDto<>(true, list);
    }

    @Path("{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Account getOne(@PathParam("id") Long id) {
        return accountsService.findById(id);
    }

    @Path("{id}/disable")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public void disable(@PathParam("id") Long id, final DisableRequestDto disableRequestDto) {
        accountsService.disable(id, disableRequestDto);
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public AccountResponseDto create(AccountDto accountDto) {
        final Account createdAccount = accountsService.save(accountDto);
        return AccountResponseDto.builder()
                .id(createdAccount.getId())
                .firstName(createdAccount.getFirstName())
                .lastName(createdAccount.getLastName())
                .balance(createdAccount.getBalance().getAmount())
                .createdAt(createdAccount.getCreatedAt())
                .lastUpdate(createdAccount.getLastUpdate())
                .build();
    }

}
