package com.revolut.repositories;

import com.revolut.model.Account;
import com.revolut.model.Sequences;
import com.revolut.model.WireTransference;
import com.revolut.model.tables.WireTransfer;
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

import static com.revolut.model.tables.Account.ACCOUNT;
import static com.revolut.model.tables.WireTransfer.WIRE_TRANSFER;

public class WireTransferenceRepositoryImpl implements WireTransferenceRepository {

    private final DataBaseHelper dataBaseHelper;

    @Inject
    public WireTransferenceRepositoryImpl(final DataBaseHelper dataBaseHelper) {
        this.dataBaseHelper = dataBaseHelper;
    }


    public void save(WireTransference transference) {
        final Date moment = new Date(Calendar.getInstance().getTime().getTime());
        try (final Connection connection = dataBaseHelper.getConnection()) {
            final Long id = DSL.using(connection).nextval(Sequences.S_WIRE_TRANSFER_ID);
            DSL.using(connection)
                    .insertInto(WIRE_TRANSFER)
                    .columns(WIRE_TRANSFER.ID,
                            WIRE_TRANSFER.TARGET_ACCOUNT_ID,
                            WIRE_TRANSFER.ORIGIN_ACCOUNT_ID,
                            WIRE_TRANSFER.AMOUNT,
                            WIRE_TRANSFER.CREATED_AT,
                            WIRE_TRANSFER.STATUS,
                            WIRE_TRANSFER.ERROR)
                    .values(id.intValue(), transference.getTarget().getId().intValue(),
                            transference.getOrigin().getId().intValue(),
                            transference.getAmount().getAmount(),
                            moment, transference.getStatus().toString(), "")
                    .execute();
            transference.setId(id);
        } catch (SQLException e) {
            throw new InternalServerErrorException();
        }
    }

    public List<WireTransference> findAll() {
        try (Connection connection = dataBaseHelper.getConnection()) {

            return DSL.using(connection)
                    .select(
                            WIRE_TRANSFER.ID,
                            WIRE_TRANSFER.AMOUNT,
                            WIRE_TRANSFER.ORIGIN_ACCOUNT_ID,
                            WIRE_TRANSFER.TARGET_ACCOUNT_ID,
                            WIRE_TRANSFER.STATUS,
                            WIRE_TRANSFER.CREATED_AT
                            )
                    .from(WIRE_TRANSFER)
                    .fetch()
                    .stream()
                    .map(r ->
                            WireTransference.builder()
                                    .id(r.get(WIRE_TRANSFER.ID).longValue())
                                    .amount(Money.of(Constants.CURRENCY_UNIT,r.get(WIRE_TRANSFER.AMOUNT)))
                                    .originAccountId(r.get(WIRE_TRANSFER.ORIGIN_ACCOUNT_ID).longValue())
                                    .targetAccountId(r.get(WIRE_TRANSFER.TARGET_ACCOUNT_ID).longValue())
                                    .createdAt(r.get(WIRE_TRANSFER.CREATED_AT))
                                    .status(WireTransference.Status.valueOf(r.get(WIRE_TRANSFER.STATUS)))
                                    .build()
                    ).collect(Collectors.toList());
        } catch (SQLException e) {
            e.printStackTrace(System.err);
            throw new InternalServerErrorException();
        }
    }

}
