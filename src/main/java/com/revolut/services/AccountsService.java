package com.revolut.services;

import com.revolut.api.resources.dto.requests.AccountDto;
import com.revolut.api.resources.dto.requests.DisableRequestDto;
import com.revolut.model.Account;
import com.revolut.model.DisableReason;

import java.util.List;

public interface AccountsService {
    Account save(final AccountDto account);
    Account findById(final Long accountId);
    void disable(final Long accountId, final DisableRequestDto disableReason);
    List<Account> findAll();
}
