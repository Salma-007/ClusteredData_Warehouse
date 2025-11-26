package com.example.datawarehouse.exception;

public class DuplicateDealException extends RuntimeException {
    public DuplicateDealException(String dealUniqueId) {
        super("Deal with ID '" + dealUniqueId + "' already exists");
    }
}
