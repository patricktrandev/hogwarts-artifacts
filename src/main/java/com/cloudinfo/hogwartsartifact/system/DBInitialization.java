package com.cloudinfo.hogwartsartifact.system;

import com.cloudinfo.hogwartsartifact.artifact.Artifact;
import com.cloudinfo.hogwartsartifact.artifact.ArtifactRepository;
import com.cloudinfo.hogwartsartifact.wizard.Wizard;
import com.cloudinfo.hogwartsartifact.wizard.WizardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DBInitialization implements CommandLineRunner
{
    private final ArtifactRepository artifactRepository;
    private final WizardRepository wizardRepository;

    @Override
    public void run(String... args) throws Exception {
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
}
