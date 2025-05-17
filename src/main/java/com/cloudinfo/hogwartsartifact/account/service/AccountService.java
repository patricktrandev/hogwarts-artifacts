package com.cloudinfo.hogwartsartifact.account.service;

import com.cloudinfo.hogwartsartifact.account.model.Account;
import com.cloudinfo.hogwartsartifact.account.model.Role;

import java.util.List;

public interface AccountService {
    Role createRole(Role role);
    Account createAccount(Account account);
    Account findByAccountId(String id);
    List<Account> findAllAccounts();
    Account updateRole(String id, String roleName);
    Account removeRoleToAccountByAdmin(String id, String roleName);


}
