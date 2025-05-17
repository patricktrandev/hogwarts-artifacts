package com.cloudinfo.hogwartsartifact.account.repository;

import com.cloudinfo.hogwartsartifact.account.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Boolean existsByName(String name);
    Optional<Role> findByName(String name);

}
