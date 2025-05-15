package com.cloudinfo.hogwartsartifact.wizard;

import com.cloudinfo.hogwartsartifact.artifact.Artifact;
import com.cloudinfo.hogwartsartifact.artifact.ArtifactRepository;
import com.cloudinfo.hogwartsartifact.exception.ResourceAlreadyExistException;
import com.cloudinfo.hogwartsartifact.exception.ResourceNotFoundException;
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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.*;


@ExtendWith(MockitoExtension.class)
class WizardServiceTest {
    @Mock
    private WizardRepository wizardRepository;

    private ArtifactRepository artifactRepository= mock(ArtifactRepository.class);
    @InjectMocks
    private WizardServiceImpl wizardService;


    @BeforeEach
    void setUp() {
        //wizardService= new WizardServiceImpl(wizardRepository);

    }

    @Test
    void givenWizard_whenCreateWizard_thenReturn200Success() {
        Wizard w=new Wizard();
        w.setId("32476234");
        w.setName("New Wizard");
        given(wizardRepository.save(w)).willReturn(w);

        Wizard saved=wizardService.createWizard(w) ;
        assertThat(w.getId()).isEqualTo(saved.getId());
        assertThat(w.getName()).isEqualTo(saved.getName());

    }
    @Test
    void givenWizard_whenCreateWizard_thenReturn400NameAlreadyExist() {
        Wizard w=new Wizard();
        w.setId("32476234");
        w.setName("New Wizard");
        given(wizardRepository.existsByName(w.getName())).willThrow(new ResourceAlreadyExistException("Name", w.getName()));

        assertThrows(ResourceAlreadyExistException.class, ()->{
            wizardService.createWizard(w) ;
        });

        verify(wizardRepository, times(1)).existsByName(w.getName());

    }
    @Test
    void givenWizard_whenFindAllWizard_thenReturn200Success() {
        List<Wizard> wizardList= new ArrayList<>();
        Wizard w=new Wizard();
        w.setId("32476234");
        w.setName("New Wizard");
        Wizard w1=new Wizard();
        w1.setId("32476235");
        w1.setName("New Wizard 2");
        wizardList.add(w);
        wizardList.add(w1);
        given(wizardRepository.findAll()).willReturn(wizardList);

        List<Wizard> list= wizardRepository.findAll();

        assertThat(wizardList.size()).isEqualTo(list.size());
        verify(wizardRepository, times(1)).findAll();

    }
    @Test
    void givenWizardId_whenFindWizardById_thenReturn404NotFound(){
        String id="32476234";
        Wizard w=new Wizard();
        w.setId("32476234");
        w.setName("New Wizard");
        given(wizardRepository.findById(id)).willThrow(new ResourceNotFoundException(id));


        org.junit.jupiter.api.Assertions.assertThrows(ResourceNotFoundException.class, ()->{
            wizardRepository.findById(id);
        });
        verify(wizardRepository, times(1)).findById(id);
    }
    @Test
    void givenWizardId_whenFindWizardById_thenReturn200Success(){
        String id="32476234";
        Wizard w=new Wizard();
        w.setId("32476234");
        w.setName("New Wizard");
        given(wizardRepository.findById(id)).willReturn(Optional.of(w));

        Wizard found=wizardService.findWizardByid(id);
        assertThat(found.getName()).isEqualTo(w.getName());

        verify(wizardRepository, times(1)).findById(id);
    }
    @Test
    void givenWizardId_whenupdateWizard_thenReturn200Success(){
        String id="32476234";
        Wizard w=new Wizard();
        w.setId("32476234");
        w.setName("New Wizard");
        Wizard u=new Wizard();
        u.setId("32476234");
        u.setName("New Wizard updated");
        given(wizardRepository.findById(id)).willReturn(Optional.of(w));
        given(wizardRepository.save(w)).willReturn(u);

        Wizard found=wizardService.updateWizard(id, w);
        assertThat(found.getName()).isEqualTo(u.getName());

        verify(wizardRepository, times(1)).findById(id);
        verify(wizardRepository, times(1)).save(w);
    }
    @Test
    void givenWizardId_whenupdateWizard_thenReturn404NotFound(){
        String id="32476234";
        Wizard w=new Wizard();
        w.setId("32476234");
        w.setName("New Wizard");

        given(wizardRepository.findById(id)).willReturn(Optional.empty());


        org.junit.jupiter.api.Assertions.assertThrows(ResourceNotFoundException.class, ()->{
            wizardService.updateWizard(id, w);
        });


        verify(wizardRepository, times(1)).findById(id);
    }
    @Test
    void givenArtifactId_whenDeleteArtifact_thenReturn200SuccessStatus(){
        String id="32476234";
        Wizard w=new Wizard();
        w.setId("32476234");
        w.setName("New Wizard");
        given(wizardRepository.findById(id)).willReturn(Optional.of(w));
        willDoNothing().given(wizardRepository).deleteById(id);

        wizardService.deleteWizard(id);
        verify(wizardRepository, times(1)).deleteById(id);
    }
    @Test
    void givenArtifactId_whenDeleteArtifact_thenReturn404NotFound(){
        String id="32476234";
        Wizard w=new Wizard();
        w.setId("32476234");
        w.setName("New Wizard");
        given(wizardRepository.findById(id)).willThrow(new ResourceNotFoundException(id));


        org.junit.jupiter.api.Assertions.assertThrows(ResourceNotFoundException.class, ()->{
           wizardService.deleteWizard(id);
        });
        verify(wizardRepository, times(1)).findById(id);
    }
    @Test
    void givenArtifactIdAndWizardId_whenAssignArtifactToWizard_thenReturn200SuccessStatus(){
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
        w.addArtifatc(a);

        Wizard w1=new Wizard();
        w1.setId("32476235");
        w1.setName("New Wizard");

       given(artifactRepository.findById("123242354325")).willReturn(Optional.of(a));
       given(wizardRepository.findById("32476235")).willReturn(Optional.of(w1));

        wizardService.assignToWizard(id, artifactId);
//
        assertThat(a.getOwner().getId()).isEqualTo("32476235");
        assertThat(w1.getArtifacts()).contains(a);

    }
}