package com.cloudinfo.hogwartsartifact.exception;

public class ResourceNotFoundException extends RuntimeException{


    public ResourceNotFoundException(String message) {
        super("Could not found with Id "+message);
    }
}
