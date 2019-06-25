package com.revolut.repositories;

import com.revolut.model.Sequences;
import com.revolut.model.TransactionLog;
import com.revolut.util.Constants;
import com.revolut.util.DataBaseHelper;
import org.joda.money.Money;
import org.jooq.impl.DSL;

import javax.inject.Inject;
import javax.ws.rs.InternalServerErrorException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

import static com.revolut.model.Tables.TRANSACTION_LOG;

public class TransactionsLogsRepositoryImpl implements TransactionsLogsRepository {

    private final DataBaseHelper dataBaseHelper;

    @Inject
    public TransactionsLogsRepositoryImpl(final DataBaseHelper dataBaseHelper) {
        this.dataBaseHelper = dataBaseHelper;
    }

    public void save(final TransactionLog transactionLog) {
        final Date moment = new Date(Calendar.getInstance().getTime().getTime());
        try (final Connection connection = dataBaseHelper.getConnection()) {
            final Long id = DSL.using(connection).nextval(Sequences.S_TRANSACTION_LOG_ID);
            DSL.using(connection)
                    .insertInto(TRANSACTION_LOG)
                    .columns(TRANSACTION_LOG.ID,
                            TRANSACTION_LOG.ACCOUNT_ID,
                            TRANSACTION_LOG.AMOUNT,
                            TRANSACTION_LOG.CREATED_AT,
                            TRANSACTION_LOG.TYPE)
                    .values(id.intValue(),
                            transactionLog.getAccount().getId().intValue(),
                            transactionLog.getAmount().getAmount(),
                            moment,
                            transactionLog.getType().toString())
                    .execute();
        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }
    }

    @Override
    public List<TransactionLog> findByAccount(final Long accountId, int limit) {
        try (final Connection connection = dataBaseHelper.getConnection()) {
            return DSL.using(connection)
                    .select(
                            TRANSACTION_LOG.ID,
                            TRANSACTION_LOG.AMOUNT,
                            TRANSACTION_LOG.CREATED_AT,
                            TRANSACTION_LOG.TYPE)
                    .from(TRANSACTION_LOG)
                    .where(TRANSACTION_LOG.ACCOUNT_ID.eq(accountId.intValue()))
                    .limit(limit)
                    .fetch()
                    .stream()
                    .map(r -> TransactionLog.builder()
                            .id(r.get(TRANSACTION_LOG.ID).longValue())
                            .amount(Money.of(Constants.CURRENCY_UNIT, r.get(TRANSACTION_LOG.AMOUNT)))
                            .createdAt(r.get(TRANSACTION_LOG.CREATED_AT))
                            .type(TransactionLog.Type.valueOf(r.get(TRANSACTION_LOG.TYPE)))
                            .build())
                    .collect(Collectors.toList());
        } catch (SQLException e) {
            e.printStackTrace(System.err);
            throw new InternalServerErrorException();
        }
    }
}
