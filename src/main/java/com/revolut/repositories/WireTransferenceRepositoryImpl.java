package com.revolut.repositories;

import com.revolut.model.Sequences;
import com.revolut.model.WireTransference;
import com.revolut.util.DataBaseHelper;
import org.jooq.impl.DSL;

import javax.inject.Inject;
import javax.ws.rs.InternalServerErrorException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.util.Calendar;

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

}
