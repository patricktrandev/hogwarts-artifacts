package com.cloudinfo.hogwartsartifact.exception;

import com.cloudinfo.hogwartsartifact.system.Response;
import com.cloudinfo.hogwartsartifact.system.StatusCode;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ArtifactNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    Response handleArtifactNotFoundException(ArtifactNotFoundException ex){
        return new Response(false, StatusCode.NOT_FOUND, ex.getMessage());
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    Response handleValidationException(MethodArgumentNotValidException ex){
        List<ObjectError> errorList= ex.getBindingResult().getAllErrors();
        Map<String, String> map= new HashMap<>(errorList.size());
        errorList.forEach(error -> {
            String key= ((FieldError) error).getField();
            String val= error.getDefaultMessage();
            map.put(key, val);
        });
        return new Response(false, StatusCode.INVALID_ARGUMENT, "Provided arguments are invalid.",map);
    }
}
