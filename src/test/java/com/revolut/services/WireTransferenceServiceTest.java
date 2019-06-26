package com.revolut.services;

import com.revolut.exception.AccountDisabledException;
import com.revolut.exception.InvalidAmountException;
import com.revolut.exception.NotFundsException;
import com.revolut.model.Account;
import com.revolut.model.DisableReason;
import com.revolut.model.TransactionLog;
import com.revolut.model.WireTransference;
import com.revolut.repositories.AccountsRepository;
import com.revolut.repositories.TransactionsLogsRepository;
import com.revolut.repositories.WireTransferenceRepository;
import com.revolut.api.resources.dto.requests.WireTransferenceRequestDto;
import com.revolut.api.resources.dto.responses.WireTransferenceResultDto;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class WireTransferenceServiceTest {

    @Mock
    private AccountsRepository accountsRepository;

    @Mock
    private TransactionsLogsRepository transactionsLogsRepository;

    @Mock
    private WireTransferenceRepository wireTransferenceRepository;

    @InjectMocks
    private WireTransferenceServiceImpl subject;

    @Mock
    private Account origin;

    @Mock
    private Account target;

    @Mock
    private WireTransference wireTransference;

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void simpleTransferSuccessfullyTest() {
        final Money money = Money.of(CurrencyUnit.EUR, 1000d);
        when(origin.isEnabled()).thenReturn(true);
        when(origin.getBalance()).thenReturn(money);
        when(origin.getId()).thenReturn(1L);
        when(target.isEnabled()).thenReturn(true);
        when(target.getId()).thenReturn(2L);

        WireTransferenceRequestDto request;
        request = WireTransferenceRequestDto.builder()
                .amount(money.getAmount())
                .origin(origin)
                .target(target)
                .build();

        final WireTransferenceResultDto transferenceResultDto = subject.transfer(request);
        assertThat(transferenceResultDto.isSuccess()).isTrue();
        verify(accountsRepository, atLeast(1)).update(any(Account.class));
        verify(transactionsLogsRepository, atLeast(2)).save(any(TransactionLog.class));
        verify(wireTransferenceRepository, atLeast(1)).save(any(WireTransference.class));
    }

    @Test(expected = NotFundsException.class)
    public void transferWithoutFundsTest() {
        final Money money = Money.of(CurrencyUnit.EUR, 0);
        when(origin.isEnabled()).thenReturn(true);
        when(origin.getBalance()).thenReturn(money);
        when(origin.getId()).thenReturn(1L);
        when(target.isEnabled()).thenReturn(true);
        when(target.getId()).thenReturn(2L);
        WireTransferenceRequestDto request;
        request = WireTransferenceRequestDto.builder()
                .amount(Money.of(CurrencyUnit.EUR, 100).getAmount())
                .origin(origin)
                .target(target)
                .build();

        final WireTransferenceResultDto transferenceResultDto = subject.transfer(request);
        assertThat(transferenceResultDto.isSuccess()).isTrue();
        verify(accountsRepository, atLeast(1)).update(any(Account.class));
        verify(transactionsLogsRepository, atLeast(2)).save(any(TransactionLog.class));
        verify(wireTransferenceRepository, atLeast(1)).save(any(WireTransference.class));
    }

    @Test(expected = InvalidAmountException.class)
    public void transferWithInvalidAmountFundsTest() {
        final Money money = Money.of(CurrencyUnit.EUR, 100);
        when(origin.isEnabled()).thenReturn(true);
        when(origin.getId()).thenReturn(1L);
        when(target.isEnabled()).thenReturn(true);
        when(target.getId()).thenReturn(2L);
        WireTransferenceRequestDto request;
        request = WireTransferenceRequestDto.builder()
                .amount(Money.of(CurrencyUnit.EUR, 0).getAmount())
                .origin(origin)
                .target(target)
                .build();

        final WireTransferenceResultDto transferenceResultDto = subject.transfer(request);
        assertThat(transferenceResultDto.isSuccess()).isTrue();
        verify(accountsRepository, atLeast(1)).update(any(Account.class));
        verify(transactionsLogsRepository, atLeast(2)).save(any(TransactionLog.class));
        verify(wireTransferenceRepository, atLeast(1)).save(any(WireTransference.class));
    }

    @Test(expected = AccountDisabledException.class)
    public void transferFromOriginDisabledAccountTest() {
        final Money money = Money.of(CurrencyUnit.EUR, 1000d);
        final Long anyId = 123L;
        when(origin.isEnabled()).thenReturn(false);
        when(origin.getId()).thenReturn(anyId);
        when(origin.getDisabledReason()).thenReturn(DisableReason.BLOCKED);

        WireTransferenceRequestDto request;
        request = WireTransferenceRequestDto.builder()
                .amount(money.getAmount())
                .origin(origin)
                .target(target)
                .build();

        subject.transfer(request);
    }

    @Test(expected = AccountDisabledException.class)
    public void transferFromTargetDisabledAccountTest() {
        final Money money = Money.of(CurrencyUnit.EUR, 1000d);
        when(origin.isEnabled()).thenReturn(true);
        when(origin.getId()).thenReturn(1L);
        when(target.getDisabledReason()).thenReturn(DisableReason.BLOCKED);
        when(target.isEnabled()).thenReturn(false);
        when(target.getId()).thenReturn(2L);

        WireTransferenceRequestDto request;
        request = WireTransferenceRequestDto.builder()
                .amount(money.getAmount())
                .origin(origin)
                .target(target)
                .build();

        subject.transfer(request);
    }
}
