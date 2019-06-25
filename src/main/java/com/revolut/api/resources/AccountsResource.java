package com.revolut.api.resources;


import com.revolut.api.resources.dto.SearchResultDto;
import com.revolut.model.Account;
import com.revolut.services.AccountsService;

import javax.inject.Inject;
import javax.ws.rs.GET;
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
}
