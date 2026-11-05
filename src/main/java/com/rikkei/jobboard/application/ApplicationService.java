package com.rikkei.jobboard.application;
import com.rikkei.jobboard.application.dto.*;
import com.rikkei.jobboard.common.exception.*;
import com.rikkei.jobboard.job.*;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ApplicationService {
    private final ApplicationRepository applicationRepository;
    private final JobRepository jobRepository;
    public ApplicationService(ApplicationRepository a, JobRepository j){ this.applicationRepository=a; this.jobRepository=j; }

    private Job requireJob(Long jobId){
        return jobRepository.findById(jobId).orElseThrow(()->new ResourceNotFoundException("Job not found with id: "+jobId));
    }

    public ApplicationResponse apply(Long jobId, ApplicationRequest req){
        Job job = requireJob(jobId);
        if (job.getStatus() == JobStatus.CLOSED) {
            throw new ConflictException("Job is closed and no longer accepting applications");
        }
        if (applicationRepository.existsByJobIdAndEmail(jobId, req.email())) {
            throw new ConflictException("Email already applied for this job");
        }
        return ApplicationMapper.toResponse(applicationRepository.save(ApplicationMapper.toEntity(req, job)));
    }

    @Transactional(readOnly=true)
    public Page<ApplicationResponse> getByJobId(Long jobId, Pageable pageable){
        requireJob(jobId);
        return applicationRepository.findByJobId(jobId, pageable).map(ApplicationMapper::toResponse);
    }
}
