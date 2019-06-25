package com.revolut.repositories;

import com.revolut.model.Account;

import java.util.List;
import java.util.Optional;

public interface AccountsRepository {

    void update(Account account);
    void save(Account account);
    Optional<Account> getById(final Long accountId);
    List<Account> findAll();

}
