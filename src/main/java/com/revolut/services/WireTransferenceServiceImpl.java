package com.revolut.services;

import com.revolut.api.resources.dto.requests.WireTransferenceRequestDto;
import com.revolut.api.resources.dto.responses.WireTransferenceResultDto;
import com.revolut.exception.AccountDisabledException;
import com.revolut.exception.DataValidationException;
import com.revolut.exception.InvalidAmountException;
import com.revolut.exception.NotFundsException;
import com.revolut.model.Account;
import com.revolut.model.TransactionLog;
import com.revolut.model.WireTransference;
import com.revolut.repositories.AccountsRepository;
import com.revolut.repositories.TransactionsLogsRepository;
import com.revolut.repositories.WireTransferenceRepository;
import com.revolut.util.Constants;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Singleton
public class WireTransferenceServiceImpl implements WireTransferenceService {

    private final TransactionsLogsRepository transactionsLogsRepository;
    private final AccountsRepository accountsRepository;
    private final WireTransferenceRepository wireTransferenceRepository;

    @Inject
    public WireTransferenceServiceImpl(final TransactionsLogsRepository transactionsLogsRepository,
                                       final AccountsRepository accountsRepository,
                                       final WireTransferenceRepository wireTransferenceRepository) {
        this.transactionsLogsRepository = transactionsLogsRepository;
        this.accountsRepository = accountsRepository;
        this.wireTransferenceRepository = wireTransferenceRepository;
    }

    private void validateAccountsIsEnabled(final Account account) {
        if (!account.isEnabled()) {
            throw new AccountDisabledException(new StringBuilder("Account: ").append(account.getId())
                    .append(" is disabled!")
                    .toString());
        }
    }

    private void checkMandatoryFields(WireTransferenceRequestDto requestDto) {
        if (Objects.isNull(requestDto.getAmount())
                || Objects.isNull(requestDto.getOrigin())
                || Objects.isNull(requestDto.getTarget())) {
            throw new DataValidationException("Mandatory fields are missing!");
        }

        if (requestDto.getOrigin().getId().equals(requestDto.getTarget().getId())) {
            throw new DataValidationException("Cannot transfer to the same account!");
        }
    }

    public synchronized WireTransferenceResultDto transfer(final WireTransferenceRequestDto wireTransferenceRequest) {
        checkMandatoryFields(wireTransferenceRequest);
        final Money amount = Money.of(Constants.CURRENCY_UNIT, wireTransferenceRequest.getAmount());
        final Date moment = Calendar.getInstance().getTime();
        final WireTransference transference = WireTransference.builder()
                .amount(amount)
                .createdAt(moment)
                .origin(wireTransferenceRequest.getOrigin())
                .target(wireTransferenceRequest.getTarget())
                .build();

        validateAccountsIsEnabled(transference.getOrigin());
        validateAccountsIsEnabled(transference.getTarget());

        if (!amount.isGreaterThan(Constants.ZERO)) {
            saveTransference(transference, WireTransference.Status.FAILED, moment);
            throw new InvalidAmountException("Amount must be greater than zero!");
        }

        final Account origin = transference.getOrigin();
        if (Objects.isNull(origin.getBalance())
                || Constants.ZERO.isEqual(origin.getBalance())
                || transference.getAmount().isGreaterThan(origin.getBalance())) {
            saveTransference(transference, WireTransference.Status.FAILED, moment);
            throw new NotFundsException(new StringBuilder("Account: ")
                    .append(origin.getId())
                    .append(" does not has sufficient funds!").toString());
        }

        final Account target = wireTransferenceRequest.getTarget();

        setBalance(target, transference.getAmount(), OperationType.ADD, moment);
        setBalance(origin, transference.getAmount(), OperationType.SUBTRACT, moment);
        saveTransference(transference, WireTransference.Status.COMPLETED, moment);

        return WireTransferenceResultDto.builder()
                .success(true)
                .wireTransferenceId(transference.getId())
                .build();
    }

    private void saveTransference(final WireTransference wireTransference,
                                  final WireTransference.Status status,
                                  final Date moment) {
        wireTransference.setCreatedAt(moment);
        wireTransference.setStatus(status);
        wireTransferenceRepository.save(wireTransference);
    }

    private void setBalance(final Account account,
                            final Money amount,
                            final OperationType operationType,
                            final Date moment) {
        final TransactionLog transactionLog = TransactionLog.builder()
                .account(account)
                .amount(amount)
                .createdAt(moment)
                .build();
        BigDecimal balance = Objects.isNull(account.getBalance()) ? BigDecimal.ZERO : account.getBalance().getAmount();
        if (OperationType.ADD.equals(operationType)) {
            balance = balance.add(amount.getAmount());
            transactionLog.setType(TransactionLog.Type.CREDIT);
        }
        if (OperationType.SUBTRACT.equals(operationType)) {
            balance = balance.subtract(amount.getAmount());
            transactionLog.setType(TransactionLog.Type.DEBIT);
        }
        account.setLastUpdate(moment);
        account.setBalance(Money.of(CurrencyUnit.EUR, balance));

        accountsRepository.update(account);
        transactionsLogsRepository.save(transactionLog);
    }

    private enum OperationType {
        ADD, SUBTRACT
    }

    public List<WireTransference> findAll(){
        return wireTransferenceRepository.findAll();
    }
}
