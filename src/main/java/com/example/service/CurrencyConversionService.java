package com.example.service;

import com.example.examplefeature.CurrencyConversionDTO;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CurrencyConversionService {

    // Taxas relativas a EUR (exemplo simplificado, valores fixos)
    private static final Map<String, Double> RATES_TO_EUR = new HashMap<>();
    static {
        RATES_TO_EUR.put("EUR", 1.0);
        RATES_TO_EUR.put("USD", 1.08);
        RATES_TO_EUR.put("GBP", 0.85);
        RATES_TO_EUR.put("JPY", 140.0);
        RATES_TO_EUR.put("CHF", 0.98);
        RATES_TO_EUR.put("AUD", 1.60);
    }

    public CurrencyConversionDTO convert(CurrencyConversionDTO dto) {
        if (dto == null) {
            throw new IllegalArgumentException("DTO não pode ser nulo");
        }

        String base = dto.getBaseCurrency();
        double amount = dto.getAmount();
        List<String> targets = dto.getTargetCurrencies();

        Map<String, Double> converted = new HashMap<>();
        if (base == null || base.isBlank() || targets == null || targets.isEmpty()) {
            dto.setConvertedAmounts(converted);
            return dto;
        }

        Double baseRate = RATES_TO_EUR.get(base.toUpperCase());
        if (baseRate == null) {
            // moeda base desconhecida -> retorna vazio
            dto.setConvertedAmounts(converted);
            return dto;
        }

        for (String target : targets) {
            if (target == null || target.isBlank()) continue;
            String t = target.toUpperCase();
            Double targetRate = RATES_TO_EUR.get(t);
            if (targetRate == null) continue;
            // converter: amount * (rateTarget / rateBase)
            double value = amount * (targetRate / baseRate);
            converted.put(t, value);
        }

        dto.setConvertedAmounts(converted);
        return dto;
    }
}
