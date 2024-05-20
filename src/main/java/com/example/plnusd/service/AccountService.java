package com.example.plnusd.service;

import com.example.plnusd.dto.CreateAccountDTO;
import com.example.plnusd.dto.ExchangeMoneyDTO;
import com.example.plnusd.dto.NBPExchangeRatesDTO;
import com.example.plnusd.entity.Account;
import com.example.plnusd.enums.ExchangeMode;
import com.example.plnusd.exception.AccountNotFoundException;
import com.example.plnusd.exception.NotEnoughMoneyException;
import com.example.plnusd.exception.TooYoungException;
import com.example.plnusd.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Service
public class AccountService {



    AccountRepository accountRepository;
    PeselService peselService;
    NBPRequestService NBPRequestService;

    @Autowired
    public AccountService(AccountRepository accountRepository, PeselService peselService, NBPRequestService NBPRequestService) {
        this.accountRepository = accountRepository;
        this.peselService = peselService;
        this.NBPRequestService = NBPRequestService;

    }

    public Account createNewAccount(CreateAccountDTO createAccountDTO) {
        LocalDate now = LocalDate.now();
        LocalDate dateOfBirth = peselService.getLocalDateFromPesel(createAccountDTO.getPesel());

        long age = ChronoUnit.YEARS.between(dateOfBirth, now);
        if(age<18) {
            throw new TooYoungException("Person too young");
        }


        Account account = Account.builder()
                .pesel(createAccountDTO.getPesel())
                .name(createAccountDTO.getName())
                .surname(createAccountDTO.getSurname())
                .PLNAmount(createAccountDTO.getPlnAmount())
                .USDAmount(BigDecimal.ZERO)
                .build();
        return accountRepository.save(account);
    }

    @Transactional
    public Account exchangeMoney(ExchangeMoneyDTO exchangeMoneyDTO) {
        Account account = findByPesel(exchangeMoneyDTO.getPesel());

        if(exchangeMoneyDTO.getExchangeMode().equals(ExchangeMode.SELL)) {
            BigDecimal maxToSellAmount = account.getAmountByCurrency(exchangeMoneyDTO.getExchangeFrom());
            if(maxToSellAmount.compareTo(exchangeMoneyDTO.getAmount()) < 0) {
                throw new NotEnoughMoneyException("Not enough money to exchange from");
            }
        }

        NBPExchangeRatesDTO exchangeRatesFrom = NBPRequestService.getExchangeRates(exchangeMoneyDTO.getExchangeFrom());
        NBPExchangeRatesDTO exchangeRatesTo = NBPRequestService.getExchangeRates(exchangeMoneyDTO.getExchangeTo());

        if(exchangeMoneyDTO.getExchangeMode().equals(ExchangeMode.SELL)) {
            //first we change it to PLN
            BigDecimal amountConverted = exchangeMoneyDTO.getAmount();
            if(exchangeRatesFrom != null) {
                amountConverted = exchangeRatesFrom.getRates().get(0).getBid().multiply(amountConverted);
            }
            //second we change it to DESIRED currency
            if(exchangeRatesTo != null) {
                amountConverted = amountConverted.divide(exchangeRatesTo.getRates().get(0).getAsk(),2, RoundingMode.FLOOR);
            }
            account.setAmountByCurrency(exchangeMoneyDTO.getExchangeFrom(), account.getAmountByCurrency(exchangeMoneyDTO.getExchangeFrom()).subtract(exchangeMoneyDTO.getAmount()).setScale(2,RoundingMode.FLOOR));
            account.setAmountByCurrency(exchangeMoneyDTO.getExchangeTo(), account.getAmountByCurrency(exchangeMoneyDTO.getExchangeTo()).add(amountConverted).setScale(2,RoundingMode.FLOOR));

        }

        if(exchangeMoneyDTO.getExchangeMode().equals(ExchangeMode.BUY)) {
            BigDecimal amountConverted = exchangeMoneyDTO.getAmount();
            if(exchangeRatesTo != null) {
                amountConverted = amountConverted.multiply(exchangeRatesTo.getRates().get(0).getAsk());
            }
            if(exchangeRatesFrom != null) {
                amountConverted = amountConverted.divide(exchangeRatesFrom.getRates().get(0).getBid(),2, RoundingMode.CEILING);
            }
            BigDecimal maxToSellAmount = account.getAmountByCurrency(exchangeMoneyDTO.getExchangeFrom());
            if(maxToSellAmount.compareTo(amountConverted) < 0) {
                throw new NotEnoughMoneyException("Not enough money to exchange from");
            }
            account.setAmountByCurrency(exchangeMoneyDTO.getExchangeFrom(), account.getAmountByCurrency(exchangeMoneyDTO.getExchangeFrom()).subtract(amountConverted).setScale(2,RoundingMode.FLOOR));
            account.setAmountByCurrency(exchangeMoneyDTO.getExchangeTo(), account.getAmountByCurrency(exchangeMoneyDTO.getExchangeTo()).add(exchangeMoneyDTO.getAmount()).setScale(2,RoundingMode.FLOOR));
        }
        account = accountRepository.save(account);
        return account;
    }

    public Account findByPesel(String pesel) {
        Optional<Account> byPesel = accountRepository.findByPesel(pesel);
        if(byPesel.isEmpty()) {
            throw new AccountNotFoundException("Account not found");
        }
        return byPesel.get();
    }


}
