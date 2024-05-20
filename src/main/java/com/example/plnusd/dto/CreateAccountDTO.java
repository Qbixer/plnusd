package com.example.plnusd.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;
import org.hibernate.validator.constraints.pl.PESEL;

import java.math.BigDecimal;

@Data
public class CreateAccountDTO {
    
    @NotNull
    @PESEL
    String pesel;

    @NotBlank
    String name;

    @NotBlank
    String surname;

    @NotNull
    @PositiveOrZero
    BigDecimal plnAmount;

}
