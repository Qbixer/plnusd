package com.example.plnusd.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NBPExchangeRatesDTO {

    @Data
    public static class Rate {
        BigDecimal ask;
        BigDecimal bid;
    }

    String code;

    List<Rate> rates;

}
