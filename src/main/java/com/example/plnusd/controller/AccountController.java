package com.example.plnusd.controller;

import com.example.plnusd.dto.CreateAccountDTO;
import com.example.plnusd.dto.ExchangeMoneyDTO;
import com.example.plnusd.entity.Account;
import com.example.plnusd.exception.SameCurrencyException;
import com.example.plnusd.service.AccountService;
import com.example.plnusd.service.NBPRequestService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/account")
public class AccountController {

    AccountService accountService;
    NBPRequestService NBPRequestService;


    @Autowired
    public AccountController(AccountService accountService, NBPRequestService NBPRequestService) {
        this.accountService = accountService;
        this.NBPRequestService = NBPRequestService;
    }


    @PostMapping("")
    public Account createNewAccount(@Valid @RequestBody CreateAccountDTO createAccountDTO) {
        return accountService.createNewAccount(createAccountDTO);
    }

    @GetMapping("/{pesel}")
    public Account getAccountByPesel(@PathVariable String pesel) {
        return accountService.findByPesel(pesel);
    }

    @PostMapping("/exchange")
    public Account exchange(@Valid @RequestBody ExchangeMoneyDTO exchangeMoneyDTO) {
        if(exchangeMoneyDTO.getExchangeFrom().equals(exchangeMoneyDTO.getExchangeTo())) {
            throw new SameCurrencyException("Same currency for exchange was used");
        }

        return accountService.exchangeMoney(exchangeMoneyDTO);
    }

}
