package com.cloudinfo.hogwartsartifact.account.service;

import com.cloudinfo.hogwartsartifact.account.model.Account;
import com.cloudinfo.hogwartsartifact.exception.ResourceAlreadyExistException;
import com.cloudinfo.hogwartsartifact.account.model.Role;
import com.cloudinfo.hogwartsartifact.account.repository.RoleRepository;
import com.cloudinfo.hogwartsartifact.account.repository.AccountRepository;
import com.cloudinfo.hogwartsartifact.exception.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Transactional
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;
    private final RoleRepository roleRepository;

    @Override
    public Role createRole(Role role) {
        Boolean found= roleRepository.existsByName(role.getName());
        if(found){
            throw new ResourceAlreadyExistException(role.getName(), "Name");
        }
        Role newRole= new Role();
        newRole.setName(role.getName().toUpperCase());

        return roleRepository.save(newRole);
    }

    @Override
    public Account createAccount(Account account) {
        Boolean found= accountRepository.existsByUsername(account.getUsername());
        Role foundRole= roleRepository.findByName("USER").orElseThrow(()-> new ResourceNotFoundException("USER"));
        if(found){
            throw new ResourceAlreadyExistException(account.getUsername(), "username");
        }
        account.setEnabled(true);
        account.setType(foundRole.getName());
        return accountRepository.save(account);
    }

    @Override
    public Account findByAccountId(String id) {
        UUID uuid = UUID.fromString(id);
        Account found= accountRepository.findById(uuid).orElseThrow(()-> new ResourceNotFoundException(id));

        return found;
    }

    @Override
    public List<Account> findAllAccounts() {
        List<Account> accountList= accountRepository.findAll();
        return accountList;
    }

    @Override
    public Account updateRole(String id, String roleName) {
        UUID uuid = UUID.fromString(id);
        String name=roleName.toUpperCase();
        Account found= accountRepository.findById(uuid).orElseThrow(()-> new ResourceNotFoundException(id));
        Role foundRole= roleRepository.findByName(name).orElseThrow(()-> new ResourceNotFoundException(roleName));

        String foundRoleName= found.getType();
        String[] splitRole= foundRoleName.split("_");
        Set<String> roleSet= new HashSet<>();
        for(String r: splitRole){
            roleSet.add(r);
        }
        roleSet.add(foundRole.getName().toUpperCase());
        StringBuilder resultRole= new StringBuilder();
        for(String r: roleSet){
            resultRole.append(r).append("_");
        }
        found.setType(String.valueOf(resultRole));
        return accountRepository.save(found);
    }




    @Override
    public Account removeRoleToAccountByAdmin(String id, String roleName) {
        UUID uuid = UUID.fromString(id);
        String name=roleName.toUpperCase();
        Account found= accountRepository.findById(uuid).orElseThrow(()-> new ResourceNotFoundException(id));
        Role foundRole= roleRepository.findByName(name).orElseThrow(()-> new ResourceNotFoundException(roleName));

        String foundRoleName= found.getType();
        if(!foundRoleName.contains(name)){
            throw new ResourceNotFoundException(name);
        }
        String[] splitRole= foundRoleName.split("_");
        Set<String> roleSet= new HashSet<>();
        for(String r: splitRole){
            roleSet.add(r);
        }
        if(roleSet.contains(name)){
            roleSet.remove(name);
        }
        StringBuilder resultRole= new StringBuilder();
        for(String r: roleSet){
            resultRole.append(r).append("_");
        }
        found.setType(String.valueOf(resultRole));
        return accountRepository.save(found);

    }
}
