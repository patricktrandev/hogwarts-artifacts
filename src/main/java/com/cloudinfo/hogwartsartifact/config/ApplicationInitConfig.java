package com.cloudinfo.hogwartsartifact.config;

import com.cloudinfo.hogwartsartifact.account.model.Account;
import com.cloudinfo.hogwartsartifact.account.repository.AccountRepository;
import com.cloudinfo.hogwartsartifact.system.RoleUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class ApplicationInitConfig {
    private final PasswordEncoder passwordEncoder;

    @Bean
    ApplicationRunner applicationRunner(AccountRepository accountRepository){
        return args->{
            if(accountRepository.findByUsername("admin").isEmpty()){
                Account account= new Account();
                account.setUsername("admin");
                account.setEmail("admin123@gmail.com");
                account.setPassword(passwordEncoder.encode("admin123"));
                account.setRole(RoleUser.ADMIN.name());
                account.setEnabled(true);
                accountRepository.save(account);
                log.info("admin user has been created");
            }
        };
    }
}
