package com.rikkei.jobboard.application.dto;
import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.URL;
public record ApplicationRequest(
        @NotBlank @Size(max=255) String fullName,
        @NotBlank @Email @Size(max=255) String email,
        @NotBlank @URL @Size(max=500) String cvUrl) {}
