package com.rikkei.jobboard.job.dto;
import com.rikkei.jobboard.job.JobStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;
public record JobResponse(Long id, Long companyId, String title, String description,
                          BigDecimal salaryMin, BigDecimal salaryMax, String location,
                          JobStatus status, LocalDateTime createdAt, LocalDateTime updatedAt) {}
