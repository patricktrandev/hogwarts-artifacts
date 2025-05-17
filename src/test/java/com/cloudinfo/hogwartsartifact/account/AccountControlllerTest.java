package com.cloudinfo.hogwartsartifact.account;

import com.cloudinfo.hogwartsartifact.account.model.Account;
import com.cloudinfo.hogwartsartifact.account.model.Role;
import com.cloudinfo.hogwartsartifact.account.service.AccountService;
import com.cloudinfo.hogwartsartifact.exception.ResourceAlreadyExistException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AccountControlller.class)
@AutoConfigureMockMvc(addFilters = false)
class AccountControlllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private AccountService userService;
    @Autowired
    private ObjectMapper objectMapper;
    @BeforeEach
    void setUp() {

    }

    @Test
    void givenRoleName_whenCreateRoleByAdmin_thenReturn201Created() throws Exception {
        Role r= new Role();
        r.setId(UUID.fromString("f8c3de3d-1fea-4d7c-a8b0-29f63c4c3454"));
        r.setName("ADMIN");
        given(userService.createRole(any(Role.class))).willReturn(r);
        ResultActions response= mockMvc.perform(post("/api/v1/roles")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(r)));
        response.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Create role successfully"))
                .andExpect(jsonPath("$.data.name").value(r.getName()));
    }
    @Test
    void givenRoleName_whenCreateRoleByAdmin_thenReturn400AlReadyExist() throws Exception {
        Role r= new Role();
        r.setId(UUID.fromString("f8c3de3d-1fea-4d7c-a8b0-29f63c4c3454"));
        r.setName("ADMIN");
        given(userService.createRole(any(Role.class))).willThrow(new ResourceAlreadyExistException(r.getName(), "Name"));
        ResultActions response= mockMvc.perform(post("/api/v1/roles")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(r)));
        response.andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Name was already exist with ADMIN"))
                .andExpect(jsonPath("$.data").isEmpty());
    }
    @Test
    void givenAccount_whenCreateAccountByAnonymous_thenReturn200Success() throws Exception {

        Role r= new Role();
        r.setId(UUID.fromString("f8c3de3d-1fea-4d7c-a8b0-29f63c4c3454"));
        r.setName("USER");
        Role r2= new Role();
        r2.setId(UUID.fromString("f8c3de3d-1fea-4d7c-a8b0-29f63c4c3454"));
        r2.setName("USER_EVENT");
        Account a= new Account();
        a.setId(UUID.fromString("663604a6-5291-4f51-bb89-bb9c13d3578a"));
        a.setUsername("testuser123");
        a.setEnabled(true);
        a.setType(r.getName());

        //a.setRoles(roles);
        given(userService.createAccount(any(Account.class))).willReturn(a);
        ResultActions response= mockMvc.perform(post("/api/v1/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(a)));
        response.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Create account successfully"));
    }
    @Test
    void givenAccount_whenFindAllAccountByAdmin_thenReturn200Success() throws Exception {
        List<Account> accountList= new ArrayList<>();
        Set<Role> roles= new HashSet<>();
        Role r= new Role();
        r.setId(UUID.fromString("f8c3de3d-1fea-4d7c-a8b0-29f63c4c3454"));
        r.setName("USER");
        Role r2= new Role();
        r2.setId(UUID.fromString("f8c3de3d-1fea-4d7c-a8b0-29f63c4c3454"));
        r2.setName("USER_EVENT");
        Account a= new Account();
        a.setId(UUID.fromString("663604a6-5291-4f51-bb89-bb9c13d3578a"));
        a.setUsername("testuser123");
        a.setEnabled(true);
        roles.add(r);
        roles.add(r2);
        a.setType(r.getName());
        Account a1= new Account();
        a1.setId(UUID.fromString("ecb8dd43-165e-4b45-bff7-f6bebd59bf0d"));
        a1.setUsername("testuser123");
        a1.setEnabled(true);
        a1.setType(r.getName());
        accountList.add(a);
        accountList.add(a1);

        given(userService.findAllAccounts()).willReturn(accountList);
        ResultActions response= mockMvc.perform(get("/api/v1/accounts").accept(MediaType.APPLICATION_JSON));
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Find all accounts successfully"))
                .andExpect(jsonPath("$.data.size()").value(accountList.size()));
    }
    @Test
    void givenAccount_whenFindAccountByAdmin_thenReturn200Success() throws Exception {

        Set<Role> roles= new HashSet<>();
        Role r= new Role();
        r.setId(UUID.fromString("f8c3de3d-1fea-4d7c-a8b0-29f63c4c3454"));
        r.setName("USER");
        Role r2= new Role();
        r2.setId(UUID.fromString("f8c3de3d-1fea-4d7c-a8b0-29f63c4c3454"));
        r2.setName("USER_EVENT");
        Account a= new Account();
        a.setId(UUID.fromString("663604a6-5291-4f51-bb89-bb9c13d3578a"));
        a.setUsername("testuser123");
        a.setEnabled(true);
        roles.add(r);
        roles.add(r2);
        a.setType(r.getName());
        //a.setRoles(roles);


        given(userService.findByAccountId("663604a6-5291-4f51-bb89-bb9c13d3578a")).willReturn(a);
        ResultActions response= mockMvc.perform(get("/api/v1/accounts/{id}","663604a6-5291-4f51-bb89-bb9c13d3578a").accept(MediaType.APPLICATION_JSON));
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Find one successfully"))
                .andExpect(jsonPath("$.data.username").value(a.getUsername()));
    }
}