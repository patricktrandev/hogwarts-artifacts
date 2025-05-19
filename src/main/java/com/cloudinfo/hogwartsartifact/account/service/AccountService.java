package com.cloudinfo.hogwartsartifact.account.service;

import com.cloudinfo.hogwartsartifact.account.dto.AccountDto;
import com.cloudinfo.hogwartsartifact.account.dto.TokenPair;
import com.cloudinfo.hogwartsartifact.account.model.Account;
import com.cloudinfo.hogwartsartifact.account.model.Role;
import com.nimbusds.jose.JOSEException;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

public interface AccountService {
    Role createRole(Role role);
    Account createAccount(Account account);
    Account findByAccountId(String id);
    Account findByAccountUsername(String name);
    List<Account> findAllAccounts();
    Account updateRole(String id, String roleName);
    Account removeRoleToAccountByAdmin(String id, String roleName);
    TokenPair authenticate(Account accountRequestAuth);
    boolean introspect(TokenPair pair) throws JOSEException, ParseException;

}
