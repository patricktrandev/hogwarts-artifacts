package com.cloudinfo.hogwartsartifact.account.converter;

import com.cloudinfo.hogwartsartifact.account.dto.AccountDto;
import com.cloudinfo.hogwartsartifact.account.dto.RoleDto;
import com.cloudinfo.hogwartsartifact.account.model.Account;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class AccountMapper implements Converter<Account, AccountDto> {
    private final RoleMapper roleMapper;
    @Override
    public AccountDto convert(Account source) {
        //var rolesList= source.getRoles()!=null? source.getRoles().stream().map(roleMapper::convert).collect(Collectors.toSet()):null;
        AccountDto account= new AccountDto(
                String.valueOf(source.getId()),
                source.getUsername(),
                source.getEmail(),
                source.getRole(),
                source.isEnabled(),
                source.getNumberOfRoles(),
                source.getCreatedAt()!=null?source.getCreatedAt():null,
                source.getCreatedBy()!=null?source.getCreatedBy():null,
                source.getUpdatedAt()!=null?source.getUpdatedAt():null,
                source.getUpdatedBy()!=null?source.getUpdatedBy():null
        );
        return account;
    }
}
