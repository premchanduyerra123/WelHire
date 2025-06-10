package com.welhire.shared.dto.enums;

public enum ParseStatus {
    UPLOADED,      // file saved, parse not yet started
    PROCESSING,    // parseAsync has begun
    SUCCESS,       // parsed & saved ok
    FAILURE        // parse threw an exception
}

