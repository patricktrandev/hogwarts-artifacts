package com.cloudinfo.hogwartsartifact.wizard;

import com.cloudinfo.hogwartsartifact.artifact.Artifact;
import com.cloudinfo.hogwartsartifact.exception.ResourceNotFoundException;
import com.cloudinfo.hogwartsartifact.system.StatusCode;
import com.cloudinfo.hogwartsartifact.wizard.converter.WizardDtoMapper;
import com.cloudinfo.hogwartsartifact.wizard.converter.WizardMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = WizardController.class)
@AutoConfigureMockMvc(addFilters = false)
class WizardControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private WizardService wizardService;

    @Autowired
    private ObjectMapper objectMapper;

    private List<Wizard> wizardList;
    @BeforeEach
    void setUp() {
        wizardList= new ArrayList<>();
        Wizard w=new Wizard();
        w.setId("32476234");
        w.setName("New Wizard");
        Wizard w1=new Wizard();
        w1.setId("32476235");
        w1.setName("New Wizard 2");
        wizardList.add(w);
        wizardList.add(w1);
    }
    @Test
    void givenWizard_whenfindAllWizards_thenReturnSuccess() throws Exception{
        BDDMockito.given(wizardService.findAllWizards()).willReturn(wizardList);

        ResultActions response=mockMvc.perform(get("/api/v1/wizards"));

        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.size()").value(wizardList.size()));
    }

    @Test
    void givenWizard_whenCreateWizard_thenReturnSuccess() throws Exception {
        Wizard w=new Wizard();
        w.setId("32476234");
        w.setName("New Wizard");

        given(wizardService.createWizard(any(Wizard.class))).willReturn(w);
        ResultActions response= mockMvc.perform(post("/api/v1/wizards")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(w)));
        response.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Create wizard successfully"))
                .andExpect(jsonPath("$.data.name").value(w.getName()));

    }

    @Test
    void givenWizardId_whenFindWizardById_thenReturnSuccess() throws Exception {
        String id="32476234";


        given(wizardService.findWizardByid(id)).willReturn(wizardList.get(0));
        ResultActions response= mockMvc.perform(get("/api/v1/wizards/{id}", id));
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Find one wizard successfully"))
                .andExpect(jsonPath("$.data.name").value(wizardList.get(0).getName()));

    }
    @Test
    void givenWizardId_whenFindWizardById_thenReturnNotFound() throws Exception {
        String id="32476234";


        given(wizardService.findWizardByid(id)).willThrow(new ResourceNotFoundException(id));
        ResultActions response= mockMvc.perform(get("/api/v1/wizards/{id}", id));
        response.andDo(print())
                .andExpect(status().isNotFound());


    }
    @Test
    void givenWizardId_whenUpdateWizard_thenReturnSuccess() throws Exception {
        String id="32476234";
        Wizard w=new Wizard();
        w.setId(id);
        w.setName("New Wizard ");
        Wizard u=new Wizard();
        u.setId(id);
        u.setName("New Wizard updated");

        given(wizardService.updateWizard(Mockito.eq(id),any(Wizard.class))).willReturn(u);
        ResultActions response= mockMvc.perform(put("/api/v1/wizards/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(w)));
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Update wizard successfully"))
                .andExpect(jsonPath("$.data.name").value(u.getName()));

    }
    @Test
    void givenWizardId_whenDeleteWizard_thenReturn200Success() throws Exception {
        String id="32476234";
        Wizard w=new Wizard();
        w.setId(id);
        w.setName("New Wizard ");
        BDDMockito.given(wizardService.findWizardByid(id)).willReturn(w);
        BDDMockito.willDoNothing().given(wizardService).deleteWizard(id);

        //when
        ResultActions response=mockMvc.perform(delete("/api/v1/wizards/{id}",id));

        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Delete wizard successfully"))
                .andExpect(jsonPath("$.data").isEmpty());
    }
    @Test
    void givenArtifactId_whenDeleteArtifact_thenReturn404NotFound() throws Exception {
        String id="32476234";

        BDDMockito.doThrow(new ResourceNotFoundException(id)).when(wizardService).deleteWizard(id);

        //when
        ResultActions response=mockMvc.perform(delete("/api/v1/wizards/{id}",id));

        response.andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void givenWizardIdAndArtifactId_whenAssignArtifactToWizard_thenReturn200Success() throws Exception {
        String id="32476235";

        String artifactId="123242354325";
        Artifact a= new Artifact();
        a.setId("123242354325");
        a.setName("new artifact");
        a.setName("An visibility Cloak of new artifact");
        a.setImageUrl("imageUrl");
        Wizard w=new Wizard();
        w.setId("32476234");
        w.setName("New Wizard");


        Wizard w1=new Wizard();
        w1.setId("32476235");
        w1.setName("New Wizard");
        w.addArtifatc(a);

        BDDMockito.given(wizardService.assignToWizard(id, artifactId)).willReturn(w1);

        //when
        ResultActions response=mockMvc.perform(put("/api/v1/wizards/{id}/artifacts/{artifactId}",id, artifactId));

        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                //.andExpect(jsonPath("$.message").value("Delete wizard successfully"))
                .andExpect(jsonPath("$.data").isNotEmpty());
    }
}