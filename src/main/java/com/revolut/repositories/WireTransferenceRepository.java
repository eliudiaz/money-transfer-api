package com.revolut.repositories;

import com.revolut.model.WireTransference;

import java.util.List;

public interface WireTransferenceRepository {

    void save(final WireTransference transference);
    List<WireTransference> findAll();
}
