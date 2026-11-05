package com.rikkei.jobboard.application;
import com.rikkei.jobboard.application.dto.*;
import jakarta.validation.Valid;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/jobs/{jobId}/applications")
public class ApplicationController {
    private final ApplicationService service;
    public ApplicationController(ApplicationService s){ this.service=s; }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApplicationResponse apply(@PathVariable Long jobId, @Valid @RequestBody ApplicationRequest req){
        return service.apply(jobId, req);
    }

    @GetMapping
    public Page<ApplicationResponse> list(@PathVariable Long jobId,
                                          @RequestParam(defaultValue="0") int page,
                                          @RequestParam(defaultValue="10") int size){
        return service.getByJobId(jobId, PageRequest.of(page, size));
    }
}
