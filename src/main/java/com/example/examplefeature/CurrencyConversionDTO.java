package com.example.examplefeature;

import java.util.List;
import java.util.Map;

public class CurrencyConversionDTO {

    private String baseCurrency;
    private double amount;
    private List<String> targetCurrencies;
    private Map<String, Double> convertedAmounts;

    public CurrencyConversionDTO() {}

    public String getBaseCurrency() {
        return baseCurrency;
    }

    public void setBaseCurrency(String baseCurrency) {
        this.baseCurrency = baseCurrency;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public List<String> getTargetCurrencies() {
        return targetCurrencies;
    }

    public void setTargetCurrencies(List<String> targetCurrencies) {
        this.targetCurrencies = targetCurrencies;
    }

    public Map<String, Double> getConvertedAmounts() {
        return convertedAmounts;
    }

    public void setConvertedAmounts(Map<String, Double> convertedAmounts) {
        this.convertedAmounts = convertedAmounts;
    }
}
