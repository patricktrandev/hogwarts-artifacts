package com.cloudinfo.hogwartsartifact.wizard;

import com.cloudinfo.hogwartsartifact.artifact.dto.ArtifactDto;
import com.cloudinfo.hogwartsartifact.system.Response;
import com.cloudinfo.hogwartsartifact.system.StatusCode;
import com.cloudinfo.hogwartsartifact.wizard.converter.WizardDtoMapper;
import com.cloudinfo.hogwartsartifact.wizard.converter.WizardMapper;
import com.cloudinfo.hogwartsartifact.wizard.dto.WizardDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("${api.endpoint.base-url}/wizards")
@RequiredArgsConstructor
public class WizardController {
    private final WizardService wizardService;
    private final WizardMapper wizardMapper;
    private final WizardDtoMapper wizardDtoMapper;
    @GetMapping
    public ResponseEntity<Response> findAllWizards(){

        List<Wizard> saved=wizardService.findAllWizards();
        List<WizardDto> dtoList= saved.stream().map(wizardMapper::convert).collect(Collectors.toList());
        return new ResponseEntity<>(new Response(true, StatusCode.SUCCESS,"Find all wizard successfully", saved), HttpStatus.OK);
    }
    @GetMapping("/{id}")
    public ResponseEntity<Response> findWizard(@PathVariable String id){

        Wizard w=wizardService.findWizardByid(id);

        return new ResponseEntity<>(new Response(true, StatusCode.SUCCESS,"Find one wizard successfully",wizardMapper.convert(w)), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Response> createWizard(@Valid @RequestBody WizardDto wizardDto){
        Wizard saved=wizardService.createWizard(wizardDtoMapper.convert(wizardDto));
        //System.out.println(saved.getName());
        //WizardDto result= wizardMapper.convert(saved);

        return new ResponseEntity<>(new Response(true, StatusCode.SUCCESS,"Create wizard successfully",wizardMapper.convert(saved)), HttpStatus.CREATED);
    }
    @PutMapping("/{id}")
    public ResponseEntity<Response> updateWizard(@Valid @RequestBody WizardDto wizardDto, @PathVariable String id){
        Wizard updated=wizardService.updateWizard(id,wizardDtoMapper.convert(wizardDto));
        //System.out.println(saved.getName());
        //WizardDto result= wizardMapper.convert(saved);

        return new ResponseEntity<>(new Response(true, StatusCode.SUCCESS,"Update wizard successfully", wizardMapper.convert(updated)), HttpStatus.OK);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Response> deleteWizard( @PathVariable String id){
        wizardService.deleteWizard(id);
        //System.out.println(saved.getName());
        //WizardDto result= wizardMapper.convert(saved);

        return new ResponseEntity<>(new Response(true, StatusCode.SUCCESS,"Delete wizard successfully"), HttpStatus.OK);
    }
    @PutMapping("/{id}/artifacts/{artifactId}")
    public ResponseEntity<Response> assignArtifactToWizard(@PathVariable String id, @PathVariable String artifactId){
        Wizard w=wizardService.assignToWizard(id, artifactId);

        return new ResponseEntity<>(new Response(true, StatusCode.SUCCESS,"Assign artifact to wizard successfully",wizardMapper.convert(w)), HttpStatus.OK);
    }
    @PutMapping("/{id}/courses/{courseId}")
    public ResponseEntity<Response> registerCourse(@PathVariable String id, @PathVariable String courseId){
        Wizard w=wizardService.registerCourse(id, courseId);

        return new ResponseEntity<>(new Response(true, StatusCode.SUCCESS,"Wizard register course successfully",wizardMapper.convert(w)), HttpStatus.OK);
    }
}
