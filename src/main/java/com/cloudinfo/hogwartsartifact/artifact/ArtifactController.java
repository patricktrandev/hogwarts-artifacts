package com.cloudinfo.hogwartsartifact.artifact;

import com.cloudinfo.hogwartsartifact.artifact.converter.ArtifactDtoMapper;
import com.cloudinfo.hogwartsartifact.artifact.converter.ArtifactMapper;
import com.cloudinfo.hogwartsartifact.artifact.dto.ArtifactDto;
import com.cloudinfo.hogwartsartifact.system.Response;
import com.cloudinfo.hogwartsartifact.system.StatusCode;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("${api.endpoint.base-url}/artifacts")
@RequiredArgsConstructor
public class ArtifactController {
    private final ArtifactService artifactService;
    private final ArtifactMapper artifactMapper;
    private final ArtifactDtoMapper artifactDtoMapper;



    @GetMapping("/{id}")
    public ResponseEntity<Response> findArtifactById(@PathVariable String id){
       ArtifactDto found = artifactMapper.convert(artifactService.findById(id));
       return new ResponseEntity<>(new Response(true, StatusCode.SUCCESS, "Find One Success", found), HttpStatus.OK);

    }
    @GetMapping
    public ResponseEntity<Response> findAllArtifacts(){
        List<Artifact> found=artifactService.findAllArtifacts();
        List<ArtifactDto> dtoList= found.stream().map(artifactMapper::convert).collect(Collectors.toList());

        return new ResponseEntity<>(new Response(true, StatusCode.SUCCESS, "Find All Success",dtoList), HttpStatus.OK);

    }
    @PostMapping
    public ResponseEntity<Response> createArtifact(@Valid @RequestBody ArtifactDto artifactDto){
        Artifact a= artifactService.saveArtifact(artifactDtoMapper.convert(artifactDto));

        ArtifactDto artifact= artifactMapper.convert(a);
        return new ResponseEntity<>(new Response(true, StatusCode.CREATED, "Create artifact Successfully",artifact), HttpStatus.CREATED);

    }
    @PutMapping("/{id}")
    public ResponseEntity<Response> updateArtifact(@Valid @RequestBody ArtifactDto artifactDto, @PathVariable String id){
        Artifact a= artifactDtoMapper.convert(artifactDto);
        Artifact updated= artifactService.updateArtifact(id, a);
        ArtifactDto artifact= artifactMapper.convert(updated);
        return new ResponseEntity<>(new Response(true, StatusCode.SUCCESS, "Updated artifact Successfully",artifact), HttpStatus.OK);

    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Response> deleteArtifactbyId( @PathVariable String id){

        artifactService.deleteArtifact(id);

        return new ResponseEntity<>(new Response(true, StatusCode.SUCCESS, "Delete artifact Successfully"), HttpStatus.OK);

    }
}
