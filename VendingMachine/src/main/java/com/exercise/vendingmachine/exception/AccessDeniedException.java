package com.exercise.vendingmachine.exception;

public class AccessDeniedException extends RuntimeException{

    public AccessDeniedException(String msg){
        super(msg);
    }

}
