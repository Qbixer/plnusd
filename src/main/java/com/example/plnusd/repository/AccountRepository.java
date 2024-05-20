package com.example.plnusd.repository;

import com.example.plnusd.entity.Account;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface AccountRepository extends CrudRepository<Account, Integer> {

    Optional<Account> findByPesel(String pesel);
}
