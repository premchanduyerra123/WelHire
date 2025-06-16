package com.welhire.exceptions;

public class CandidateCreationException extends RuntimeException {
    public CandidateCreationException(String message, Throwable cause) {
        super(message, cause);
    }
}