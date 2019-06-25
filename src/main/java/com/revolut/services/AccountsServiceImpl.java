package com.revolut.services;

import com.revolut.exception.ResourceNotFoundException;
import com.revolut.model.Account;
import com.revolut.model.TransactionLog;
import com.revolut.repositories.AccountsRepository;
import com.revolut.repositories.TransactionsLogsRepository;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

public class AccountsServiceImpl implements AccountsService {

    private AccountsRepository accountsRepository;
    private TransactionsLogsRepository transactionsLogsRepository;

    @Inject
    public AccountsServiceImpl(final AccountsRepository accountsRepository,
                               final TransactionsLogsRepository transactionsLogsRepository) {
        this.accountsRepository = accountsRepository;
        this.transactionsLogsRepository = transactionsLogsRepository;
    }

    public void save(final Account account) {
        accountsRepository.save(account);
    }

    public Account findById(final Long accountId) {
        final Optional<Account> opAccount = accountsRepository.getById(accountId);
        if (opAccount.isPresent()) {
            final Account account = opAccount.get();
            final List<TransactionLog> transactions = transactionsLogsRepository.findByAccount(accountId, 10);
            account.setRecentTransactions(transactions);
            return account;
        }
        throw new ResourceNotFoundException("There is not any account with ID: " + accountId);
    }

    public List<Account> findAll() {
        return accountsRepository.findAll();
    }
}
