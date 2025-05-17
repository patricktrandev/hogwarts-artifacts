package com.cloudinfo.hogwartsartifact.account.model;

import com.cloudinfo.hogwartsartifact.system.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "account")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class Account extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(length = 36)
    private UUID id;
    private String username;
    private String email;
    private String password;
    private String type;
    private boolean enabled;

    public int getNumberOfRoles(){
        String[] roles= this.type.split("_");
        return roles.length;
    }


}
