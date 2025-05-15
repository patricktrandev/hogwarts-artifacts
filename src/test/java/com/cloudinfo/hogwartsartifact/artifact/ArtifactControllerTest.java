package com.cloudinfo.hogwartsartifact.artifact;

import com.cloudinfo.hogwartsartifact.exception.ArtifactNotFoundException;
import com.cloudinfo.hogwartsartifact.system.StatusCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import static org.mockito.Mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;



import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
class ArtifactControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    ArtifactService artifactService;
    @Autowired
    private ObjectMapper objetcMapper;

    List<Artifact> artifactList;

    @BeforeEach
    void setUp() {
        this.artifactList= new ArrayList<>();
        Artifact a1=new Artifact();
        a1.setId("234325436");
        a1.setName("Test Artifact 234325436");
        a1.setDescription("Test Artifact Descripition 234325436");
        a1.setImageUrl("imageUrl");
        Artifact a2=new Artifact();
        a2.setId("54634523");
        a2.setName("Test Artifact 54634523");
        a2.setDescription("Test Artifact Descripition 54634523");
        a2.setImageUrl("imageUrl");
        Artifact a3=new Artifact();
        a3.setId("234235345");
        a3.setName("Test Artifact 234235345");
        a3.setDescription("Test Artifact Descripition 234235345");
        a3.setImageUrl("imageUrl");
        artifactList.add(a1);
        artifactList.add(a2);
        artifactList.add(a3);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testFindArtifactByIdSuccess() throws Exception {
        //given
        String id="234235345";
        BDDMockito.given(artifactService.findById(id)).willReturn(artifactList.get(2));


        //when
        ResultActions response =mockMvc.perform(get("/api/v1/artifacts/{id}", id)
                .accept(MediaType.APPLICATION_JSON));
        //then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find One Success"))
                .andExpect(jsonPath("$.data.id").value(id));
        verify(artifactService, times(1)).findById(id);
    }
    @Test
    void testFindArtifactByIdNotFound() throws Exception {
        //given
        String id="234235345";
        BDDMockito.given(artifactService.findById(id)).willThrow(new ArtifactNotFoundException(id));
        //when
        ResultActions response =mockMvc.perform(get("/api/v1/artifacts/{id}", id)
                .accept(MediaType.APPLICATION_JSON));
        //then
        response.andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not found artifact with Id "+id))
                .andExpect(jsonPath("$.data").isEmpty());
        verify(artifactService, times(1)).findById(id);
    }
    @Test
    void givenArtifactList_whenFindAllArtifacts_thenReturnArtifactList() throws Exception {
        BDDMockito.given(artifactService.findAllArtifacts()).willReturn(artifactList);

        //when
        ResultActions response=mockMvc.perform(get("/api/v1/artifacts"));

        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find All Success"))
                .andExpect(jsonPath("$.data.size()").value(artifactList.size()));
    }
    @Test
    void givenArtifactObject_whenCreateArtifact_thenReturnArtifactObject() throws Exception {
        Artifact a= new Artifact();
        a.setId("3478324732-ffd87f6f8ds");
        a.setName("new artifact");
        a.setDescription("An visibility Cloak of new artifact");
        a.setImageUrl("imageUrl");
        BDDMockito.given(artifactService.saveArtifact(ArgumentMatchers.any(Artifact.class))).willReturn(a);

        //when
        ResultActions response=mockMvc.perform(post("/api/v1/artifacts").contentType(MediaType.APPLICATION_JSON)
                .content(objetcMapper.writeValueAsString(a)));

        response.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.CREATED))
                .andExpect(jsonPath("$.message").value("Create artifact Successfully"))
                .andExpect(jsonPath("$.data.name").value(a.getName()));
    }
    @Test
    void givenArtifacObjectAndId_whenUpdateArtifact_thenReturnArtifactObject() throws Exception {
        String id="324324532-3724";
        Artifact a= new Artifact();
        a.setId(id);
        a.setName("new artifact");
        a.setDescription("An visibility Cloak of new artifact");
        a.setImageUrl("imageUrl");
        Artifact u= new Artifact();
        u.setId(id);
        u.setName("new artifact updated ");
        u.setName("An visibility Cloak of new artifact updated");
        u.setImageUrl("imageUrl");
        BDDMockito.given(artifactService.updateArtifact(eq(id), any(Artifact.class))).willReturn(u);

        //when
        ResultActions response=mockMvc.perform(put("/api/v1/artifacts/{id}",id).contentType(MediaType.APPLICATION_JSON)
                .content(objetcMapper.writeValueAsString(a)));

        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Updated artifact Successfully"))
                .andExpect(jsonPath("$.data.name").value(u.getName()));
    }
    @Test
    void givenArtifacObjectAndId_whenUpdateArtifact_thenReturnNonExistentId() throws Exception {
        String id="324324532-3724";
        Artifact a= new Artifact();
        a.setId(id);
        a.setName("new artifact");
        a.setDescription("An visibility Cloak of new artifact");
        a.setImageUrl("imageUrl");

        BDDMockito.given(artifactService.updateArtifact(eq(id), any(Artifact.class))).willThrow(new ArtifactNotFoundException(id));

        //when
        ResultActions response=mockMvc.perform(put("/api/v1/artifacts/{id}",id).contentType(MediaType.APPLICATION_JSON)
                .content(objetcMapper.writeValueAsString(a)));

        response.andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.data").isEmpty());
    }
    @Test
    void givenArtifactId_whenDeleteArtifact_thenReturn200Success() throws Exception {
        String id="324324532-3724";
        Artifact a= new Artifact();
        a.setId(id);
        a.setName("new artifact");
        a.setDescription("An visibility Cloak of new artifact");
        a.setImageUrl("imageUrl");
        BDDMockito.given(artifactService.findById(id)).willReturn(a);
        BDDMockito.willDoNothing().given(artifactService).deleteArtifact(id);

        //when
        ResultActions response=mockMvc.perform(delete("/api/v1/artifacts/{id}",id));

        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Delete artifact Successfully"))
                .andExpect(jsonPath("$.data").isEmpty());
    }
    @Test
    void givenArtifactId_whenDeleteArtifact_thenReturn404NotFound() throws Exception {
        String id="324324532-3724";

        BDDMockito.doThrow(new ArtifactNotFoundException(id)).when(artifactService).deleteArtifact(id);

        //when
        ResultActions response=mockMvc.perform(delete("/api/v1/artifacts/{id}",id));

        response.andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.data").isEmpty());
    }


}