package com.graph.theory.brute.force.technique;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class CurrencyExchange {

    private Currency currency;
    private Map<Currency,Double> currencyExchangeRates;

    public CurrencyExchange(Currency fromCurrency,Currency toCurrency, Double exchangeRate ){

        currency = fromCurrency;
        currencyExchangeRates = new HashMap<>();
        currencyExchangeRates.put(toCurrency,exchangeRate);

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CurrencyExchange that = (CurrencyExchange) o;
        return currency == that.currency;
    }

    @Override
    public int hashCode() {
        return Objects.hash(currency);
    }

    public void addExchangeRate(Currency toCurrency, Double exchangeRate){

        currencyExchangeRates.put(toCurrency,exchangeRate);

    }

    public Currency getCurrency() {
        return currency;
    }

    public Map<Currency, Double> getCurrencyExchangeRates() {
        return Collections.unmodifiableMap(currencyExchangeRates);
    }
}
