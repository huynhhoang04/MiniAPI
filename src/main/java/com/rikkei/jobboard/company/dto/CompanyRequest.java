package com.rikkei.jobboard.company.dto;
import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.URL;
public record CompanyRequest(
        @NotBlank @Size(max=255) String name,
        @Size(max=255) String address,
        @URL @Size(max=255) String website) {}
