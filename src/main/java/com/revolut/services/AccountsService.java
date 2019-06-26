package com.revolut.services;

import com.revolut.api.resources.dto.requests.CreateAccountRequestDto;
import com.revolut.model.Account;

import java.util.List;

public interface AccountsService {
    Account save(final CreateAccountRequestDto account);
    Account findById(final Long accountId);
    void disable(final Long accountId);
    List<Account> findAll();
}
