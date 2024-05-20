package com.example.plnusd.service;

import com.example.plnusd.dto.NBPExchangeRatesDTO;
import com.example.plnusd.enums.Currency;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class NBPRequestService {

    RestTemplate restTemplate;

    public NBPRequestService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    public NBPExchangeRatesDTO getExchangeRates(Currency currency) {
        if(currency.equals(Currency.PLN)) {
            return null;
        }
        String url = "https://api.nbp.pl/api/exchangerates/rates/c/"+currency;
        return this.restTemplate.getForObject(url, NBPExchangeRatesDTO.class);
    }
}
