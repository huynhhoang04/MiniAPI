package com.rikkei.jobboard.application.dto;
import java.time.LocalDateTime;
public record ApplicationResponse(Long id, Long jobId, String fullName, String email, String cvUrl, LocalDateTime appliedAt) {}
