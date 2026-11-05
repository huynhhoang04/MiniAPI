package com.rikkei.jobboard.job;
import com.rikkei.jobboard.common.exception.*;
import com.rikkei.jobboard.company.Company;
import com.rikkei.jobboard.company.CompanyRepository;
import com.rikkei.jobboard.job.dto.*;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Set;

@Service
@Transactional
public class JobService {
    private static final Set<String> ALLOWED_SORT_FIELDS = Set.of("title","salaryMin","salaryMax","createdAt","id");
    private final JobRepository jobRepository;
    private final CompanyRepository companyRepository;
    public JobService(JobRepository j, CompanyRepository c){ this.jobRepository=j; this.companyRepository=c; }

    private Company requireCompany(Long id){
        return companyRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Company not found with id: "+id));
    }
    private Job requireJob(Long id){
        return jobRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Job not found with id: "+id));
    }

    public JobResponse create(JobRequest req){
        Company company = requireCompany(req.companyId());
        return JobMapper.toResponse(jobRepository.save(JobMapper.toEntity(req, company)));
    }
    @Transactional(readOnly=true)
    public JobResponse getById(Long id){ return JobMapper.toResponse(requireJob(id)); }

    public JobResponse update(Long id, JobRequest req){
        Job j = requireJob(id);
        Company company = requireCompany(req.companyId());
        JobMapper.update(j, req, company);
        return JobMapper.toResponse(jobRepository.save(j));
    }
    public void delete(Long id){ jobRepository.delete(requireJob(id)); }

    @Transactional(readOnly=true)
    public Page<JobResponse> search(String keyword, String location, JobStatus status, Pageable pageable){
        validateSort(pageable);
        return jobRepository.findAll(JobSpecification.search(keyword, location, status), pageable).map(JobMapper::toResponse);
    }
    private void validateSort(Pageable pageable){
        for (Sort.Order o : pageable.getSort()) {
            if (!ALLOWED_SORT_FIELDS.contains(o.getProperty())) {
                throw new BadRequestException("Sorting by '" + o.getProperty() + "' is not supported. Allowed: title, salaryMin, salaryMax, createdAt");
            }
        }
    }
}
