package com.graph.theory.brute.force.technique;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

/* Assumptions:
 * Proper Data
 * cycles doesn't matter anymore as its a graph
 */
/*ToDo:
 * Even if one path is satisfied, there could be a path that needs to be satisfied, so lets run everywhere at start and refine further
 * think more on paths, there could be a possibility that once you have visited a path, you need not revisit it.
 */
public class CurrencyExchangeValidator {

    private static Map<Currency, CurrencyExchange> currencyExchangeHolder = new HashMap<>();
    private final static String fileName = "currency-exchange-rates.csv";
    private static Boolean isValidationSuccessful = true;


    public static void main(String[] args){

        initCurrencyExchangeHolder();

        currencyExchangeHolder.entrySet().stream().forEach(currencyExchangeEntry -> {

            Currency startCurrency = currencyExchangeEntry.getKey();
            Double startingCurrencyValue = 1.0;
            StringBuilder pathTraversed = new StringBuilder();

            System.out.println("\nValidating start entity=" + startCurrency);
            StringBuilder executionPath = new StringBuilder();
            executionPath.append("Dinar");

            validateCurrencyExchangeRates(startCurrency, startingCurrencyValue, startCurrency, null, startingCurrencyValue, executionPath);

        });

        if (!isValidationSuccessful) {
            System.out.println("validation failed");
        } else {
            System.out.println("validation successful");
        }
    }

    private static void validateCurrencyExchangeRates(Currency startingCurrency, Double startingCurrencyValue,
                                                      Currency fromCurrency, Currency toCurrency,
                                                      Double currentCurrencyValuation, StringBuilder executionPath) {

        if (!isValidationSuccessful) {

        } else if (toCurrency == null) {

            for (Map.Entry<Currency, Double> currencyExchangeRate : currencyExchangeHolder.get(fromCurrency).getCurrencyExchangeRates().entrySet()) {

                StringBuilder executionPathNew = new StringBuilder();

                executionPathNew.append(fromCurrency);
                executionPathNew.append("->");
                executionPathNew.append(currencyExchangeRate.getKey());

                validateCurrencyExchangeRates(startingCurrency, startingCurrencyValue, fromCurrency, currencyExchangeRate.getKey(), currentCurrencyValuation, executionPathNew);
            }

        } else if (startingCurrency == toCurrency) {

            System.out.println(executionPath);

            currentCurrencyValuation = currentCurrencyValuation * currencyExchangeHolder.get(fromCurrency).getCurrencyExchangeRates().get(toCurrency);

            if (!startingCurrencyValue.equals(currentCurrencyValuation)) {

                if (Math.abs(startingCurrencyValue - currentCurrencyValuation) > .04)

                    isValidationSuccessful = false;

            }


        } else {

            currentCurrencyValuation = currentCurrencyValuation * currencyExchangeHolder.get(fromCurrency).getCurrencyExchangeRates().get(toCurrency);

            for (Map.Entry<Currency, Double> currencyExchangeRate : currencyExchangeHolder.get(toCurrency).getCurrencyExchangeRates().entrySet()) {

                //adding to skip never ending cyclic graph
                if (executionPath.indexOf(currencyExchangeRate.getKey().toString()) != -1 && !currencyExchangeRate.getKey().equals(startingCurrency)) {

                    System.out.println("skipping cycles,current path=" + executionPath + "->" + currencyExchangeRate.getKey());

                    continue;
                }

                StringBuilder executionPathNew = new StringBuilder(executionPath);
                executionPathNew.append("->");
                executionPathNew.append(currencyExchangeRate.getKey());
                validateCurrencyExchangeRates(startingCurrency, startingCurrencyValue, toCurrency, currencyExchangeRate.getKey(), currentCurrencyValuation, executionPathNew);

            }
        }

    }

    private static void initCurrencyExchangeHolder() {

        try {
            Path path = Paths.get(CurrencyExchangeValidator.class.getClassLoader()
                    .getResource(fileName).toURI());
            Stream<String> stream = Files.lines(path);
            stream.forEach(currencyExchangeRate -> {

                String[] currencyExchangeRateSplitted = currencyExchangeRate.split(",");
                Currency fromCurrency = Currency.valueOf(currencyExchangeRateSplitted[0]);
                Currency toCurrency = Currency.valueOf(currencyExchangeRateSplitted[1]);
                Double exchangeRate = Double.valueOf(currencyExchangeRateSplitted[2]);

                if (currencyExchangeHolder.containsKey(fromCurrency)) {

                    currencyExchangeHolder.get(fromCurrency).addExchangeRate(toCurrency, exchangeRate);

                } else {

                    currencyExchangeHolder.put(fromCurrency, new CurrencyExchange(fromCurrency, toCurrency, exchangeRate));

                }

            });

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}