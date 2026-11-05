package com.rikkei.jobboard.job;
import com.rikkei.jobboard.job.dto.*;
import jakarta.validation.Valid;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/jobs")
public class JobController {
    private final JobService service;
    public JobController(JobService s){ this.service=s; }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public JobResponse create(@Valid @RequestBody JobRequest req){ return service.create(req); }

    @GetMapping("/{id}")
    public JobResponse getById(@PathVariable Long id){ return service.getById(id); }

    @GetMapping
    public Page<JobResponse> search(@RequestParam(required=false) String keyword,
                                    @RequestParam(required=false) String location,
                                    @RequestParam(required=false) JobStatus status,
                                    @RequestParam(defaultValue="0") int page,
                                    @RequestParam(defaultValue="10") int size,
                                    @RequestParam(required=false) String sort){
        Pageable pageable = PageRequest.of(page, size, parseSort(sort));
        return service.search(keyword, location, status, pageable);
    }
    private Sort parseSort(String sort){
        if(sort==null||sort.isBlank()) return Sort.unsorted();
        String[] p=sort.split(",");
        String field=p[0].trim();
        Sort.Direction dir = p.length>1 && p[1].trim().equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        return Sort.by(dir, field);
    }

    @PutMapping("/{id}")
    public JobResponse update(@PathVariable Long id, @Valid @RequestBody JobRequest req){ return service.update(id, req); }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id){ service.delete(id); }
}
