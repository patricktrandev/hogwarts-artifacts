package com.cloudinfo.hogwartsartifact.account.converter;

import com.cloudinfo.hogwartsartifact.account.dto.RoleDto;
import com.cloudinfo.hogwartsartifact.account.model.Role;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class RoleMapper implements Converter<Role, RoleDto> {

    @Override
    public RoleDto convert(Role source) {
        RoleDto role= new RoleDto(
                String.valueOf(source.getId()),
                source.getName()
        );

        return role;
    }
}
