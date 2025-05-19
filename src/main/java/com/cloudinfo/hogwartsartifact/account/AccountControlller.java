package com.cloudinfo.hogwartsartifact.account;

import com.cloudinfo.hogwartsartifact.account.converter.AccountMapper;
import com.cloudinfo.hogwartsartifact.account.dto.AccountDto;
import com.cloudinfo.hogwartsartifact.account.dto.TokenPair;
import com.cloudinfo.hogwartsartifact.account.model.Account;
import com.cloudinfo.hogwartsartifact.system.Response;
import com.cloudinfo.hogwartsartifact.system.StatusCode;
import com.cloudinfo.hogwartsartifact.account.model.Role;
import com.cloudinfo.hogwartsartifact.account.service.AccountService;
import com.nimbusds.jose.JOSEException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("${api.endpoint.base-url}")
@RequiredArgsConstructor
public class AccountControlller {
    private static final Logger log = LoggerFactory.getLogger(AccountControlller.class);
    private final AccountService accountService;
    private final AccountMapper accountMapper;
    //private static final Logger log= LoggerFactory.getLogger(AccountControlller.class);

    @PostMapping("/roles")
    public ResponseEntity<Response> createRoleByAdmin(@RequestBody Role role){
        Role saved= accountService.createRole(role);
        return new ResponseEntity<>(new Response(true, StatusCode.CREATED,"Create role successfully",saved), HttpStatus.CREATED);
    }
    @PostMapping("/register")
    public ResponseEntity<Response> createAccountByAnonymousUser(@RequestBody Account account){
        Account savedAccount=accountService.createAccount(account);

        AccountDto saved = accountMapper.convert(savedAccount);
        return new ResponseEntity<>(new Response(true, StatusCode.CREATED,"Create account successfully"), HttpStatus.CREATED);
    }
    @PostMapping("/login")
    public ResponseEntity<Response> login(@RequestBody Account account){
        //Map<String, Object> loginInfo =accountService.loginAccount(account);
        //log.info("Authentication ---"+authentication.getName());
        TokenPair token =accountService.authenticate(account);
        return new ResponseEntity<>(new Response(true, StatusCode.SUCCESS,"users info and jwt", token),HttpStatus.OK);
    }
    @PostMapping("/introspect")
    public ResponseEntity<Response> validateToken(@RequestBody TokenPair tokenPair) throws ParseException, JOSEException {
        //Map<String, Object> loginInfo =accountService.loginAccount(account);
        //log.info("Authentication ---"+authentication.getName());
        boolean check=accountService.introspect(tokenPair);
        return new ResponseEntity<>(new Response(true, StatusCode.SUCCESS,"validate token", check),HttpStatus.OK);
    }
    @GetMapping("/accounts/{id}")
    public ResponseEntity<Response> getAccountByIdAuthorizedByAdmin(@PathVariable String id){
        AccountDto found = accountMapper.convert(accountService.findByAccountId(id));
        return new ResponseEntity<>(new Response(true, StatusCode.SUCCESS,"Find one successfully",found), HttpStatus.OK);
    }
    @GetMapping("/accounts")
    public ResponseEntity<Response> getAllAccountsAuthorizedByAdmin(){
        var authentication= SecurityContextHolder.getContext().getAuthentication();
        log.info(authentication.getName());
        authentication.getAuthorities().forEach(grantedAuthority -> log.info(grantedAuthority.getAuthority()));

        List<AccountDto> found = accountService.findAllAccounts().stream()
                .map(accountMapper::convert).collect(Collectors.toList());
        return new ResponseEntity<>(new Response(true, StatusCode.SUCCESS,"Find all accounts successfully",found), HttpStatus.OK);
    }
    @PutMapping("/accounts/{id}")
    public ResponseEntity<Response> addRoleToAccountsAuthorizedByAdmin(@PathVariable String id, @RequestBody Role role){
        AccountDto updated =accountMapper.convert(accountService.updateRole(id, role.getName()));
        return new ResponseEntity<>(new Response(true, StatusCode.SUCCESS,"Add role successfully",updated), HttpStatus.OK);
    }
    @DeleteMapping("/accounts/{id}")
    public ResponseEntity<Response> removeRoleToAccountsAuthorizedByAdmin(@PathVariable String id, @RequestBody Role role){
        AccountDto removed =accountMapper.convert(accountService.removeRoleToAccountByAdmin(id, role.getName()));
        return new ResponseEntity<>(new Response(true, StatusCode.SUCCESS,"Remove role successfully",removed), HttpStatus.OK);
    }


}
