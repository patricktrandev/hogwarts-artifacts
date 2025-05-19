package com.cloudinfo.hogwartsartifact.account.service;

import com.cloudinfo.hogwartsartifact.account.converter.AccountMapper;
import com.cloudinfo.hogwartsartifact.account.dto.AccountDto;
import com.cloudinfo.hogwartsartifact.account.dto.TokenPair;
import com.cloudinfo.hogwartsartifact.account.model.Account;
import com.cloudinfo.hogwartsartifact.account.model.Role;
import com.cloudinfo.hogwartsartifact.account.repository.AccountRepository;
import com.cloudinfo.hogwartsartifact.account.repository.RoleRepository;
import com.cloudinfo.hogwartsartifact.exception.ResourceAlreadyExistException;
import com.cloudinfo.hogwartsartifact.exception.ResourceNotFoundException;
import com.cloudinfo.hogwartsartifact.system.RoleUser;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.experimental.NonFinal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
@Transactional
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
    private static final Logger log = LoggerFactory.getLogger(AccountServiceImpl.class);
    private final AccountRepository accountRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AccountMapper accountMapper;

    //@NonFinal
    //protected static final String signKey="";
    @Value("${app.jwt.secret}")
    private String signKey;

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
        Role foundRole= roleRepository.findByName(RoleUser.USER.name()).orElseThrow(()-> new ResourceNotFoundException(RoleUser.USER.name()));
        if(found){
            throw new ResourceAlreadyExistException(account.getUsername(), "username");
        }
        account.setPassword(passwordEncoder.encode(account.getPassword()));
        account.setEnabled(true);
        account.setRole(foundRole.getName());
        return accountRepository.save(account);
    }

    @Override
    public Account findByAccountId(String id) {
        UUID uuid = UUID.fromString(id);
        Account found= accountRepository.findById(uuid).orElseThrow(()-> new ResourceNotFoundException(id));

        return found;
    }

    @Override
    public Account findByAccountUsername(String name) {
        Account account=accountRepository.findByUsername(name).orElseThrow(()-> new ResourceNotFoundException(name));
        return account;
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

        String foundRoleName= found.getRole();
        String[] splitRole= foundRoleName.split(" ");
        Set<String> roleSet= new HashSet<>();
        for(String r: splitRole){
            roleSet.add(r);
        }
        roleSet.add(foundRole.getName().toUpperCase());
        StringBuilder resultRole= new StringBuilder();
        for(String r: roleSet){
            resultRole.append(r).append("_");
        }
        found.setRole(String.valueOf(resultRole));
        return accountRepository.save(found);
    }




    @Override
    public Account removeRoleToAccountByAdmin(String id, String roleName) {
        UUID uuid = UUID.fromString(id);
        String name=roleName.toUpperCase();
        Account found= accountRepository.findById(uuid).orElseThrow(()-> new ResourceNotFoundException(id));
        Role foundRole= roleRepository.findByName(name).orElseThrow(()-> new ResourceNotFoundException(roleName));

        String foundRoleName= found.getRole();
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
        found.setRole(String.valueOf(resultRole));
        return accountRepository.save(found);

    }


    @Override
    public TokenPair authenticate(Account accountRequestAuth) {
        Account account=accountRepository.findByUsername(accountRequestAuth.getUsername()).orElseThrow(()-> new ResourceNotFoundException(accountRequestAuth.getUsername()));
        boolean check=passwordEncoder.matches(accountRequestAuth.getPassword(),account.getPassword());
        if(!check){
            throw new BadCredentialsException("401 Exception- User is not authenticated");
        }
        String token= generateToken(accountRequestAuth.getUsername(),account.getEmail(), account.getRole());
        TokenPair tokenPair= new TokenPair(token, null);

        return tokenPair;
    }

    @Override
    public boolean introspect(TokenPair pair) throws JOSEException, ParseException {
        var token= pair.accessToken();
        JWSVerifier verifier= new MACVerifier(signKey.getBytes());
        SignedJWT jwt=SignedJWT.parse(token);
        Date expiryTime= jwt.getJWTClaimsSet().getExpirationTime();
        boolean check=jwt.verify(verifier);
        return check && expiryTime.after(new Date());
    }

    private String generateToken(String username, String email,String role) {
        JWSHeader header= new JWSHeader(JWSAlgorithm.HS256);
        JWTClaimsSet jwtClaimsSet= new JWTClaimsSet.Builder()
                .subject(username)
                .issuer("cloud.tientn")
                .issueTime(new Date())
                .expirationTime(new Date(Instant.now().plus(1, ChronoUnit.HOURS).toEpochMilli()))
                .claim("email",email)
                .claim("scope",role)
                .build();
        Payload payload= new Payload(jwtClaimsSet.toJSONObject());
        JWSObject jwsObject=new JWSObject(header,payload);
        try {
            jwsObject.sign(new MACSigner(signKey));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            log.error("Cannot create token");
            throw new RuntimeException(e);
        }

    }

}
