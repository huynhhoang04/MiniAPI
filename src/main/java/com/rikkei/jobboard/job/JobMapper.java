package com.rikkei.jobboard.job;
import com.rikkei.jobboard.company.Company;
import com.rikkei.jobboard.job.dto.*;
public final class JobMapper {
    private JobMapper(){}
    public static Job toEntity(JobRequest r, Company company){
        Job j=new Job();
        j.setCompany(company); j.setTitle(r.title()); j.setDescription(r.description());
        j.setSalaryMin(r.salaryMin()); j.setSalaryMax(r.salaryMax()); j.setLocation(r.location()); j.setStatus(r.status());
        return j;
    }
    public static void update(Job j, JobRequest r, Company company){
        j.setCompany(company); j.setTitle(r.title()); j.setDescription(r.description());
        j.setSalaryMin(r.salaryMin()); j.setSalaryMax(r.salaryMax()); j.setLocation(r.location()); j.setStatus(r.status());
    }
    public static JobResponse toResponse(Job j){
        return new JobResponse(j.getId(), j.getCompany().getId(), j.getTitle(), j.getDescription(),
                j.getSalaryMin(), j.getSalaryMax(), j.getLocation(), j.getStatus(), j.getCreatedAt(), j.getUpdatedAt());
    }
}
