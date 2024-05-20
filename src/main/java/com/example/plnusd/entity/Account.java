package com.example.plnusd.entity;

import com.example.plnusd.enums.Currency;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.pl.PESEL;

import java.math.BigDecimal;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Account {
    @Id
    @GeneratedValue
    Integer id;

    @Column(unique = true, nullable = false)
    @PESEL
    @NotNull
    String pesel;

    @Column
    @NotBlank
    String name;

    @Column
    @NotBlank
    String surname;

    @Column
    @NotNull
    @PositiveOrZero
    BigDecimal PLNAmount;

    @Column
    @NotNull
    @PositiveOrZero
    BigDecimal USDAmount;

    public BigDecimal getAmountByCurrency(Currency currency) {
        switch (currency) {
            case PLN -> {
                return PLNAmount;
            }
            case USD -> {
                return USDAmount;
            }
            default -> {
                throw new IllegalArgumentException("This account doesn't exist");
            }
        }
    }

    public void setAmountByCurrency(Currency currency, BigDecimal amount) {
        switch (currency) {
            case PLN -> {
                setPLNAmount(amount);
            }
            case USD -> {
                setUSDAmount(amount);
            }
            default -> {
                throw new IllegalArgumentException("This account doesn't exist");
            }
        }
    }
}
