package com.abinbev.exceptions;

public class RecordNotFoundException extends ServiceException {
    public RecordNotFoundException(String message){
        super(message);
    }
}
