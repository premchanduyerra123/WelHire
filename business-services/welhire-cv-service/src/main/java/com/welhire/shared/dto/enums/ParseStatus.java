package com.welhire.shared.dto.enums;

public enum ParseStatus {
    CV_UPLOADED,
    CV_PROCESSING,
    CV_PARSED_SUCCESS,
    CV_PARSED_FAILURE,

    CANDIDATE_CREATION_PROCESSING,
    CANDIDATE_CREATION_SUCCESS,
    CANDIDATE_CREATION_FAILURE,

    CV_JD_MATCHING_PROCESSING,
    CV_JD_MATCHING_SUCCESS,
    CV_JD_MATCHING_FAILURE,

    UNKNOWN_ERROR // fallback       // parse threw an exception
}

