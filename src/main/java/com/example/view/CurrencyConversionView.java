package com.example.view;

import com.example.examplefeature.CurrencyConversionDTO;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Route("currency")
@PageTitle("Conversão de Moedas")
@Menu(order = 3, icon = "vaadin:money", title = "Conversão de Moedas")
public class CurrencyConversionView extends VerticalLayout {

    private final List<String> currencies = Arrays.asList("EUR", "USD", "GBP", "JPY", "CHF", "AUD");

    public CurrencyConversionView() {
        ComboBox<String> baseCurrency = new ComboBox<>("Moeda base");
        baseCurrency.setItems(currencies);
        baseCurrency.setValue("EUR");

        NumberField amountLeft = new NumberField("Valor (esquerda)");
        amountLeft.setMin(0);
        amountLeft.setValue(100.0);

        // Quatro slots para moedas alvo
        ComboBox<String> target1 = new ComboBox<>("Moeda alvo 1");
        ComboBox<String> target2 = new ComboBox<>("Moeda alvo 2");
        ComboBox<String> target3 = new ComboBox<>("Moeda alvo 3");
        ComboBox<String> target4 = new ComboBox<>("Moeda alvo 4");
        target1.setItems(currencies);
        target2.setItems(currencies);
        target3.setItems(currencies);
        target4.setItems(currencies);
        target1.setValue("USD");
        target2.setValue("GBP");
        target3.setValue("JPY");
        target4.setValue("CHF");

        // Campos de resultado (direita) read-only
        TextField result1 = new TextField();
        TextField result2 = new TextField();
        TextField result3 = new TextField();
        TextField result4 = new TextField();
        result1.setReadOnly(true);
        result2.setReadOnly(true);
        result3.setReadOnly(true);
        result4.setReadOnly(true);

        Button convert = new Button("Converter", event -> {
            try {
                CurrencyConversionDTO dto = new CurrencyConversionDTO();
                dto.setBaseCurrency(baseCurrency.getValue());
                Double amt = amountLeft.getValue();
                dto.setAmount(amt != null ? amt : 0.0);
                dto.setTargetCurrencies(Arrays.asList(
                        target1.getValue(),
                        target2.getValue(),
                        target3.getValue(),
                        target4.getValue()
                ));

                RestTemplate rest = new RestTemplate();
                CurrencyConversionDTO response = rest.postForObject(
                        "http://localhost:8080/api/currency/convert",
                        dto,
                        CurrencyConversionDTO.class
                );

                if (response != null && response.getConvertedAmounts() != null) {
                    Map<String, Double> map = response.getConvertedAmounts();
                    result1.setValue(valueOrEmpty(map.get(target1.getValue())));
                    result2.setValue(valueOrEmpty(map.get(target2.getValue())));
                    result3.setValue(valueOrEmpty(map.get(target3.getValue())));
                    result4.setValue(valueOrEmpty(map.get(target4.getValue())));
                    Notification.show("Conversão efetuada.");
                } else {
                    Notification.show("Resposta inválida do servidor.", 3000, Notification.Position.MIDDLE);
                }
            } catch (Exception e) {
                Notification.show("Erro ao converter: " + e.getMessage(), 5000, Notification.Position.MIDDLE);
            }
        });

        HorizontalLayout top = new HorizontalLayout(baseCurrency, amountLeft, convert);
        HorizontalLayout targetsRow = new HorizontalLayout(
                new VerticalLayout(target1, result1),
                new VerticalLayout(target2, result2),
                new VerticalLayout(target3, result3),
                new VerticalLayout(target4, result4)
        );

        add(top, targetsRow);
        setPadding(true);
        setSpacing(true);
    }

    private String valueOrEmpty(Double v) {
        return v != null ? String.format("%.4f", v) : "";
    }
}
