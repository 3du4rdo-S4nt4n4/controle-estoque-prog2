package com.uemg.estoque.exception;

//Nao deixa buscar produtos inexistemtes.
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
