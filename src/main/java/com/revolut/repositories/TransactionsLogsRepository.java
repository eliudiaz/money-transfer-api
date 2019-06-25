package com.revolut.repositories;

import com.revolut.model.TransactionLog;

import java.util.List;

public interface TransactionsLogsRepository {

    void save(final TransactionLog transactionLog);

    List<TransactionLog> findByAccount(final Long accountId, int limit);

}
