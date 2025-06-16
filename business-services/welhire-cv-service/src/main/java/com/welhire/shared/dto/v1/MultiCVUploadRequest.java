package com.welhire.shared.dto.v1;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class MultiCVUploadRequest {

    @NotBlank(message = "JD Content is required")
    private String jdRefId;

    @Email(message = "Must be a valid email")
    @NotBlank(message = "Email is required")
    private String email;
}

