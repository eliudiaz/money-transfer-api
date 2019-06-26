package com.revolut.services;

import com.revolut.api.resources.dto.requests.CreateAccountRequestDto;
import com.revolut.api.resources.dto.requests.DisableAccountRequestDto;
import com.revolut.exception.DataValidationException;
import com.revolut.exception.ResourceNotFoundException;
import com.revolut.model.Account;
import com.revolut.model.DisableReason;
import com.revolut.model.TransactionLog;
import com.revolut.repositories.AccountsRepository;
import com.revolut.repositories.TransactionsLogsRepository;
import com.revolut.util.Constants;
import org.joda.money.Money;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
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

    private void checkMandatoryFields(CreateAccountRequestDto requestDto) {
        if (Objects.isNull(requestDto.getInitialBalance())
                || Objects.isNull(requestDto.getFirstName())
                || Objects.isNull(requestDto.getLastName())) {
            throw new DataValidationException("Mandatory fields are missing!");
        }
        if (BigDecimal.ZERO.compareTo(requestDto.getInitialBalance()) > 0) {
            throw new DataValidationException("Initial balance greater than zero!");
        }
    }

    public Account save(final CreateAccountRequestDto account) {
        checkMandatoryFields(account);
        final Account dbAccount = Account.builder()
                .firstName(account.getFirstName())
                .lastName(account.getLastName())
                .balance(Money.of(Constants.CURRENCY_UNIT, account.getInitialBalance()))
                .previousBalance(Constants.ZERO)
                .build();
        accountsRepository.save(dbAccount);
        return dbAccount;
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

    @Override
    public void disable(Long accountId, DisableAccountRequestDto disableReason) {

        if (!Arrays.asList(DisableReason.values())
                .stream()
                .map(DisableReason::name)
                .filter(reason -> reason.equalsIgnoreCase(disableReason.getReason()))
                .findAny().isPresent()) {
            throw new DataValidationException("Unsupported reason message!");
        }

        final Account account = findById(accountId);
        account.setEnabled(false);
        account.setDisabledReason(DisableReason.valueOf(disableReason.getReason()));
        accountsRepository.update(account);
    }

    public List<Account> findAll() {
        return accountsRepository.findAll();
    }
}
