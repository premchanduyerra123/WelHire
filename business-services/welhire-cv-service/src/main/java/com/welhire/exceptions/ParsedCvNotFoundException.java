package com.welhire.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ParsedCvNotFoundException extends RuntimeException {

    private final String code;

    public ParsedCvNotFoundException(String id) {
        super("Parsed CV not found: " + id);
        this.code = "CV_NOT_FOUND";
    }
}
