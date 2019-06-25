package com.revolut.util;

import org.joda.money.CurrencyUnit;
import org.joda.money.Money;

public final class Constants {

    public static final CurrencyUnit CURRENCY_UNIT = CurrencyUnit.EUR;
    public static final Money ZERO = Money.zero(CURRENCY_UNIT);

}
