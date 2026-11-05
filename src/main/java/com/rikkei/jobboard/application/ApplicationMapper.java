package com.rikkei.jobboard.application;
import com.rikkei.jobboard.application.dto.*;
import com.rikkei.jobboard.job.Job;
import java.time.LocalDateTime;
public final class ApplicationMapper {
    private ApplicationMapper(){}
    public static Application toEntity(ApplicationRequest r, Job job){
        Application a=new Application();
        a.setJob(job); a.setFullName(r.fullName()); a.setEmail(r.email()); a.setCvUrl(r.cvUrl());
        a.setAppliedAt(LocalDateTime.now());
        return a;
    }
    public static ApplicationResponse toResponse(Application a){
        return new ApplicationResponse(a.getId(), a.getJob().getId(), a.getFullName(), a.getEmail(), a.getCvUrl(), a.getAppliedAt());
    }
}
