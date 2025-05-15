package com.cloudinfo.hogwartsartifact.artifact;


import com.cloudinfo.hogwartsartifact.artifact.converter.ArtifactDtoMapper;
import com.cloudinfo.hogwartsartifact.artifact.converter.ArtifactMapper;
import com.cloudinfo.hogwartsartifact.artifact.utils.IdWorker;
import com.cloudinfo.hogwartsartifact.exception.ResourceNotFoundException;
import com.cloudinfo.hogwartsartifact.wizard.Wizard;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.*;
@ExtendWith(MockitoExtension.class)
class ArtifactServiceTest {
    @Mock
    ArtifactRepository artifactRepository;
    @InjectMocks
    private ArtifactServiceImpl artifactService;
    @Mock
    private IdWorker idWorker;
    @Mock
    private ArtifactDtoMapper artifactDtoMapper;
    @Mock
    private ArtifactMapper artifactMapper;

    @BeforeEach
    void setUp() {
        //artifactService= new ArtifactServiceImpl(artifactRepository,idWorker);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void givenArtifactId_whenFindById_thenReturnArtifactObject() {
        //given
        Artifact a= new Artifact();
        a.setId("56751283219361");
        a.setName("Cloak");
        a.setName("An visibility Cloak");
        a.setImageUrl("imageUrl");

        Wizard w= new Wizard();
        w.setId("2");
        w.setName("Harry Potter");

        a.setOwner(w);

        given(artifactRepository.findById("56751283219361")).willReturn(Optional.of(a));
        //when
        Artifact result=artifactService.findById("56751283219361");
        //then
        assertThat(result.getId()).isEqualTo(a.getId());
        assertThat(result.getDescription()).isEqualTo(a.getDescription());
        assertThat(result.getName()).isEqualTo(a.getName());
        assertThat(result.getImageUrl()).isEqualTo(a.getImageUrl());

        verify(artifactRepository, times(1)).findById("56751283219361");
    }

    @Test
    void givenArtifactList_whenFindAll_thenReturnArtifactList(){
        Artifact a= new Artifact();
        a.setId("56751283219361");
        a.setName("Cloak");
        a.setName("An visibility Cloak");
        a.setImageUrl("imageUrl");
        Artifact a1= new Artifact();
        a1.setId("56751283219362");
        a1.setName("Cloak 56751283219362");
        a1.setName("An visibility Cloak 56751283219362 ");
        a1.setImageUrl("imageUrl");
        List<Artifact> artifactList= new ArrayList<>();
        artifactList.add(a1);
        artifactList.add(a);


        //given
        given(artifactRepository.findAll()).willReturn(artifactList);
        //when
        List<Artifact> actual= artifactService.findAllArtifacts();
        //then
        assertThat(actual).isNotNull();
        assertThat(actual.size()).isEqualTo(artifactList.size());
        verify(artifactRepository, times(1)).findAll();
    }

    @Test
    void givenArtifactObject_whenSaveArtifact_thenReturnArtifactObject(){
        Artifact a= new Artifact();
        a.setName("new artifact");
        a.setName("An visibility Cloak of new artifact");
        a.setImageUrl("imageUrl");


        given(artifactRepository.save(a)).willReturn(a);

        Artifact savedArtifact= artifactService.saveArtifact(a);

        assertThat(savedArtifact.getName()).isEqualTo(a.getName());
        verify(artifactRepository, times(1)).save(a);
    }
    @Test
    void givenArtifactObjectAndId_whenUpdateArtifact_thenReturnArtifactObject(){
        String id="123242354325";
        Artifact a= new Artifact();
        a.setId("123242354325");
        a.setName("new artifact");
        a.setName("An visibility Cloak of new artifact");
        a.setImageUrl("imageUrl");
        Artifact u= new Artifact();
        u.setId("123242354325");
        u.setName("new artifact updated ");
        u.setName("An visibility Cloak of new artifact updated");
        u.setImageUrl("imageUrl");

        given(artifactRepository.findById(id)).willReturn(Optional.of(a));
        given(artifactRepository.save(a)).willReturn(u);

        Artifact updatedArtifact= artifactService.updateArtifact(id, a);

        assertThat(updatedArtifact.getName()).isEqualTo(u.getName());
        assertThat(updatedArtifact.getId()).isEqualTo(u.getId());
        verify(artifactRepository, times(1)).save(a);
    }
    @Test
    void givenArtifactObjectAndId_whenUpdateArtifact_thenReturnNotFoundException(){
        String id="123242354325";
        Artifact a= new Artifact();
        a.setId("123242354325");
        a.setName("new artifact");
        a.setName("An visibility Cloak of new artifact");
        a.setImageUrl("imageUrl");

        given(artifactRepository.findById(id)).willReturn(Optional.empty());

        org.junit.jupiter.api.Assertions.assertThrows(ResourceNotFoundException.class, ()->{
             artifactService.updateArtifact(id, a);
        });



        verify(artifactRepository, times(1)).findById(id);
    }
    @Test
    void givenArtifactId_whenDeleteArtifact_thenReturn200SuccessStatus(){
        String id="123242354325";
        Artifact a= new Artifact();
        a.setId("123242354325");
        a.setName("new artifact");
        a.setName("An visibility Cloak of new artifact");
        a.setImageUrl("imageUrl");
        given(artifactRepository.findById(id)).willReturn(Optional.of(a));
        willDoNothing().given(artifactRepository).deleteById(id);

        artifactService.deleteArtifact(id);
        verify(artifactRepository, times(1)).deleteById(id);
    }
    @Test
    void givenArtifactId_whenDeleteArtifact_thenReturn404NotFound(){
        String id="123242354325";
        Artifact a= new Artifact();
        a.setId("123242354325");
        a.setName("new artifact");
        a.setName("An visibility Cloak of new artifact");
        a.setImageUrl("imageUrl");
        given(artifactRepository.findById(id)).willThrow(new ResourceNotFoundException(id));


        org.junit.jupiter.api.Assertions.assertThrows(ResourceNotFoundException.class, ()->{
            artifactService.deleteArtifact(id);
        });
        verify(artifactRepository, times(1)).findById(id);
    }


}