package com.revolut.services;

import com.revolut.model.Account;

import java.util.List;

public interface AccountsService {
    void save(final Account account);
    Account findById(final Long accountId);
    List<Account> findAll();
}
