package com.rikkei.jobboard.company.dto;
import java.time.LocalDateTime;
public record CompanyResponse(Long id, String name, String address, String website, LocalDateTime createdAt, LocalDateTime updatedAt) {}
