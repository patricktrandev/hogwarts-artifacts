package com.cloudinfo.hogwartsartifact.wizard;

import com.cloudinfo.hogwartsartifact.artifact.Artifact;
import com.cloudinfo.hogwartsartifact.artifact.ArtifactRepository;
import com.cloudinfo.hogwartsartifact.course.Course;
import com.cloudinfo.hogwartsartifact.course.CourseRepository;
import com.cloudinfo.hogwartsartifact.exception.ResourceAlreadyExistException;
import com.cloudinfo.hogwartsartifact.exception.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class WizardServiceImpl implements WizardService{
    private final WizardRepository wizardRepository;
    private final ArtifactRepository artifactRepository;
    private final CourseRepository courseRepository;

    @Override
    public Wizard createWizard(Wizard w) {
        Boolean found= wizardRepository.existsByName(w.getName());
        if(found){
           throw new ResourceAlreadyExistException("Name", w.getName());
        }
        UUID uuid = UUID.randomUUID();
        w.setId(uuid+"");
        Wizard wizard=wizardRepository.save(w);
        return wizard;
    }

    @Override
    public Wizard findWizardByid(String id) {
        Wizard w=wizardRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException(id));
        return w;
    }

    @Override
    public List<Wizard> findAllWizards() {
        return wizardRepository.findAll();
    }

    @Override
    public void deleteWizard(String id) {
        Wizard w=wizardRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException(id));

        w.removeAllArtifacts();
        wizardRepository.deleteById(id);
    }

    @Override
    public Wizard updateWizard(String id, Wizard convert) {
        Wizard w=wizardRepository.findById(id)
                .map(item->{
                    item.setName(convert.getName());
                    return wizardRepository.save(item);
                })
                .orElseThrow(()-> new ResourceNotFoundException(id));

        return w;
    }

    @Override
    public Wizard assignToWizard(String id, String artifactId) {
        Artifact artifact=artifactRepository.findById(artifactId).orElseThrow(()-> new ResourceNotFoundException(artifactId));
        Wizard w=wizardRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException(id));

       if(artifact.getOwner()!=null){
           //clear owner in artifact
           ///artifact.getOwner().removeArtifact(artifact);
           artifact.setOwner(null);
           w.getArtifacts().remove(artifact);
       }
        w.addArtifatc(artifact);
        //artifactRepository.save(artifact);
        return wizardRepository.save(w);
    }

    @Override
    public Wizard registerCourse(String id, String courseId) {
        Course course=courseRepository.findById(courseId).orElseThrow(()-> new ResourceNotFoundException(courseId));
        Wizard w=wizardRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException(id));
        w.addCourse(course);
        Wizard saved=wizardRepository.save(w);
        return saved;
    }
}
