package com.example.controller;

import com.example.examplefeature.CurrencyConversionDTO;
import com.example.service.CurrencyConversionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/currency")
public class CurrencyConversionController {

    private final CurrencyConversionService service;

    @Autowired
    public CurrencyConversionController(CurrencyConversionService service) {
        this.service = service;
    }

    @PostMapping("/convert")
    public CurrencyConversionDTO convert(@RequestBody CurrencyConversionDTO dto) {
        return service.convert(dto);
    }
}
