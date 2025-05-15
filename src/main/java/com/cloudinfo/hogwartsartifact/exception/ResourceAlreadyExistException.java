package com.cloudinfo.hogwartsartifact.exception;

public class ResourceAlreadyExistException extends RuntimeException{
    private String name;

    public ResourceAlreadyExistException(String message, String name) {
        super(String.format("%s was already exist with associated email %s",name, message));
        this.name = name;
    }
}
