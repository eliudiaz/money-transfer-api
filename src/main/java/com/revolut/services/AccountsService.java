package com.revolut.services;

import com.revolut.api.resources.dto.requests.AccountDto;
import com.revolut.model.Account;

import java.util.List;

public interface AccountsService {
    Account save(final AccountDto account);
    Account findById(final Long accountId);
    List<Account> findAll();
}
