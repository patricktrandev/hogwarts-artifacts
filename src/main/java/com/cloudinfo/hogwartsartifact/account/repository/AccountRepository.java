package com.cloudinfo.hogwartsartifact.account.repository;

import com.cloudinfo.hogwartsartifact.account.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface AccountRepository extends JpaRepository<Account, String> {
    Boolean existsByUsername(String name);
    Optional<Account> findById(UUID id);
    Optional<Account> findByUsername(String username);
}
