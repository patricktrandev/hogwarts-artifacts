package com.cloudinfo.hogwartsartifact.exception;

public class ArtifactNotFoundException extends RuntimeException{
    public ArtifactNotFoundException(String message) {
        super("Could not found artifact with Id "+message);
    }
}
