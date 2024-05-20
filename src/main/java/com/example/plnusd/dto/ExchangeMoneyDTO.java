package com.example.plnusd.dto;

import com.example.plnusd.enums.Currency;
import com.example.plnusd.enums.ExchangeMode;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;
import org.hibernate.validator.constraints.pl.PESEL;

import java.math.BigDecimal;

@Data
public class ExchangeMoneyDTO {
    
    @NotNull
    @PESEL
    String pesel;

    @NotNull
    Currency exchangeFrom;

    @NotNull
    Currency exchangeTo;

    @NotNull
    @PositiveOrZero
    BigDecimal amount;

    @NotNull
    ExchangeMode exchangeMode;

}
