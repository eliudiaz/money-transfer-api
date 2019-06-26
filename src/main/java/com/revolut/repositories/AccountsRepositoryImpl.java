package com.revolut.repositories;

import com.revolut.model.Account;
import com.revolut.model.Sequences;
import com.revolut.util.Constants;
import com.revolut.util.DataBaseHelper;
import org.joda.money.Money;
import org.jooq.impl.DSL;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.InternalServerErrorException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.revolut.model.tables.Account.ACCOUNT;

@Singleton
public class AccountsRepositoryImpl implements AccountsRepository {

    private DataBaseHelper dataBaseHelper;

    @Inject
    public AccountsRepositoryImpl(DataBaseHelper dataBaseHelper) {
        this.dataBaseHelper = dataBaseHelper;
    }

    public void save(Account account) {
        final Date moment = new Date(Calendar.getInstance().getTime().getTime());
        try (final Connection connection = dataBaseHelper.getConnection()) {
            final Long id = DSL.using(connection).nextval(Sequences.S_ACCOUNT_ID);
            DSL.using(connection)
                    .insertInto(ACCOUNT)
                    .columns(
                            ACCOUNT.ID,
                            ACCOUNT.FIRST_NAME,
                            ACCOUNT.LAST_NAME,
                            ACCOUNT.BALANCE,
                            ACCOUNT.PREVIOUS_BALANCE,
                            ACCOUNT.CREATED_AT,
                            ACCOUNT.LAST_UPDATE,
                            ACCOUNT.ENABLED)
                    .values(id.intValue(), account.getFirstName(), account.getLastName(),
                            account.getBalance().getAmount(), account.getPreviousBalance().getAmount(), moment, moment,
                            true)
                    .execute();
            account.setId(id);
        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }
    }

    public void update(Account account) {
        final Date moment = new Date(Calendar.getInstance().getTime().getTime());
        try (final Connection connection = dataBaseHelper.getConnection()) {
            DSL.using(connection)
                    .update(ACCOUNT)
                    .set(ACCOUNT.FIRST_NAME, account.getFirstName())
                    .set(ACCOUNT.LAST_NAME, account.getLastName())
                    .set(ACCOUNT.BALANCE, account.getBalance().getAmount())
                    .set(ACCOUNT.PREVIOUS_BALANCE, account.getPreviousBalance().getAmount())
                    .set(ACCOUNT.LAST_UPDATE, new Date(moment.getTime()))
                    .set(ACCOUNT.ENABLED, account.isEnabled())
                    .where(ACCOUNT.ID.eq(account.getId().intValue()))
                    .execute();
        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }
    }

    public Optional<Account> getById(final Long accountId) {
        try (final Connection connection = dataBaseHelper.getConnection()) {
            return DSL.using(connection)
                    .select(
                            ACCOUNT.ID,
                            ACCOUNT.FIRST_NAME,
                            ACCOUNT.LAST_NAME,
                            ACCOUNT.BALANCE,
                            ACCOUNT.PREVIOUS_BALANCE,
                            ACCOUNT.CREATED_AT,
                            ACCOUNT.LAST_UPDATE,
                            ACCOUNT.DISABLE_REASON,
                            ACCOUNT.ENABLED)
                    .from(ACCOUNT)
                    .where(ACCOUNT.ID.eq(accountId.intValue()))
                    .fetch()
                    .stream()
                    .map(r -> Account.builder()
                            .id(r.get(ACCOUNT.ID).longValue())
                            .firstName(r.get(ACCOUNT.FIRST_NAME))
                            .lastName(r.get(ACCOUNT.LAST_NAME))
                            .balance(Money.of(Constants.CURRENCY_UNIT, r.get(ACCOUNT.BALANCE)))
                            .previousBalance(Money.of(Constants.CURRENCY_UNIT, r.get(ACCOUNT.PREVIOUS_BALANCE)))
                            .createdAt(r.get(ACCOUNT.CREATED_AT))
                            .lastUpdate(r.get(ACCOUNT.LAST_UPDATE))
                            .enabled(r.get(ACCOUNT.ENABLED))
                            .build()
                    )
                    .findFirst();
        } catch (SQLException e) {
            e.printStackTrace(System.err);
            throw new InternalServerErrorException();
        }
    }


    public List<Account> findAll() {
        try (Connection connection = dataBaseHelper.getConnection()) {

            return DSL.using(connection)
                    .select(
                            ACCOUNT.ID,
                            ACCOUNT.FIRST_NAME,
                            ACCOUNT.LAST_NAME,
                            ACCOUNT.BALANCE,
                            ACCOUNT.PREVIOUS_BALANCE,
                            ACCOUNT.CREATED_AT,
                            ACCOUNT.LAST_UPDATE,
                            ACCOUNT.DISABLE_REASON,
                            ACCOUNT.ENABLED)
                    .from(ACCOUNT)
                    .fetch()
                    .stream()
                    .map(r ->
                            Account.builder()
                                    .id(r.get(ACCOUNT.ID).longValue())
                                    .firstName(r.get(ACCOUNT.FIRST_NAME))
                                    .lastName(r.get(ACCOUNT.LAST_NAME))
                                    .balance(Money.of(Constants.CURRENCY_UNIT, r.get(ACCOUNT.BALANCE)))
                                    .previousBalance(Money.of(Constants.CURRENCY_UNIT, r.get(ACCOUNT.PREVIOUS_BALANCE)))
                                    .createdAt(r.get(ACCOUNT.CREATED_AT))
                                    .lastUpdate(r.get(ACCOUNT.LAST_UPDATE))
                                    .enabled(r.get(ACCOUNT.ENABLED))
                                    .build()
                    ).collect(Collectors.toList());
        } catch (SQLException e) {
            e.printStackTrace(System.err);
            throw new InternalServerErrorException();
        }
    }

}
