package com.uemg.estoque.exception;

//Impede violação da regra de negocios
public class BusinessException extends RuntimeException {

    public BusinessException(String message) {
        super(message);
    }
}
