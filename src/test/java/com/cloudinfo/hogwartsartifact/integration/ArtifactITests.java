package com.cloudinfo.hogwartsartifact.integration;

import com.cloudinfo.hogwartsartifact.artifact.Artifact;
import com.cloudinfo.hogwartsartifact.artifact.ArtifactRepository;
import com.cloudinfo.hogwartsartifact.artifact.converter.ArtifactMapper;
import com.cloudinfo.hogwartsartifact.system.StatusCode;
import com.cloudinfo.hogwartsartifact.wizard.Wizard;
import com.cloudinfo.hogwartsartifact.wizard.WizardRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ArtifactITests {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private WizardRepository wizardRepository;
    @Autowired
    private ArtifactRepository artifactRepository;
    @Autowired
    private ArtifactMapper artifactMapper;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        Artifact a1=new Artifact();
        a1.setId("234325431");
        a1.setName("Test Artifact 234325431");
        a1.setDescription("Test Artifact Descripition 234325431");
        a1.setImageUrl("imageUrl");
        Artifact a2=new Artifact();
        a2.setId("234325432");
        a2.setName("Test Artifact 234325432");
        a2.setDescription("Test Artifact Descripition 234325432");
        a2.setImageUrl("imageUrl");
        Artifact a3=new Artifact();
        a3.setId("234325433");
        a3.setName("Test Artifact 234325433");
        a3.setDescription("Test Artifact Descripition 234325433");
        a3.setImageUrl("imageUrl");
        Artifact a4=new Artifact();
        a4.setId("234325434");
        a4.setName("Test Artifact 234325434");
        a4.setDescription("Test Artifact Descripition 234325434");
        a4.setImageUrl("imageUrl");
        Artifact a5=new Artifact();
        a5.setId("234325435");
        a5.setName("Test Artifact 234325435");
        a5.setDescription("Test Artifact Descripition 234325435");
        a5.setImageUrl("imageUrl");
        Artifact a6=new Artifact();
        a6.setId("234325436");
        a6.setName("Test Artifact 234325436");
        a6.setDescription("Test Artifact Descripition 234325436");
        a6.setImageUrl("imageUrl");


        Wizard w1= new Wizard();
        w1.setName("Test w1");
        w1.setId("43564661");
        Wizard w2= new Wizard();
        w2.setName("Test w2");
        w2.setId("43564662");
        Wizard w3= new Wizard();
        w3.setName("Test w3");
        w3.setId("43564663");

        w1.addArtifatc(a1);
        w1.addArtifatc(a3);
        w2.addArtifatc(a2);
        w2.addArtifatc(a6);
        w3.addArtifatc(a4);

        wizardRepository.save(w1);
        wizardRepository.save(w2);
        wizardRepository.save(w3);

        artifactRepository.save(a5);
    }

    @Test
    void givenArtifactId_whenFindById_thenReturnArtifactObject() throws Exception {
        String id= "234325431";
        ResultActions response =mockMvc.perform(get("/api/v1/artifacts/{id}", id)
                .accept(MediaType.APPLICATION_JSON));
        response.andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find One Success"))
                .andExpect(jsonPath("$.data.id").value(id));


    }
    @Test
    void givenArtifactList_whenFindAllArtifacts_thenReturnArtifactList() throws Exception {

        ResultActions response =mockMvc.perform(get("/api/v1/artifacts")
                .accept(MediaType.APPLICATION_JSON));
        response.andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find All Success"))
                .andExpect(jsonPath("$.data.size()").value(6));


    }
    @Test
    void givenArtifact_whenAveArtifact_thenReturnArtifactObject() throws Exception {
        Artifact a= new Artifact();
        //a.setId("3478324732-ffd87f6f8ds");
        a.setName("new artifact");
        a.setDescription("An visibility Cloak of new artifact");
        a.setImageUrl("imageUrl");
        ResultActions response =mockMvc.perform(post("/api/v1/artifacts").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(a)));
        response.andDo(MockMvcResultHandlers.print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.CREATED))
                .andExpect(jsonPath("$.message").value("Create artifact Successfully"))
                .andExpect(jsonPath("$.data.name").value(a.getName()))
                .andExpect(jsonPath("$.data.id").isNotEmpty());


    }
    @Test
    void givenArtifacObjectAndId_whenUpdateArtifact_thenReturnArtifactObject() throws Exception {
        String id="234325434"; // existing id in db

        Artifact u= new Artifact();

        u.setName("new artifact updated ");
        u.setName("An visibility Cloak of new artifact updated");
        u.setImageUrl("imageUrl");


        //when
        ResultActions response=mockMvc.perform(put("/api/v1/artifacts/{id}",id).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(u)));

        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Updated artifact Successfully"))
                .andExpect(jsonPath("$.data.name").value(u.getName()));
    }
    @Test
    void givenArtifacId_whenDeleteArtifact_thenReturn200Success() throws Exception {
        String id="234325434"; // existing id in db

        //when
        ResultActions response=mockMvc.perform(delete("/api/v1/artifacts/{id}",id));

        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Delete artifact Successfully"))
                .andExpect(jsonPath("$.data").isEmpty());
    }
}
