package com.rikkei.jobboard.job.dto;
import com.rikkei.jobboard.job.JobStatus;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record JobRequest(
        @NotNull Long companyId,
        @NotBlank @Size(max=255) String title,
        String description,
        @PositiveOrZero BigDecimal salaryMin,
        @PositiveOrZero BigDecimal salaryMax,
        @Size(max=255) String location,
        @NotNull JobStatus status) {

    @AssertTrue(message = "salaryMax must be greater than or equal to salaryMin")
    public boolean isSalaryValid() {
        return salaryMin == null || salaryMax == null || salaryMax.compareTo(salaryMin) >= 0;
    }
}
