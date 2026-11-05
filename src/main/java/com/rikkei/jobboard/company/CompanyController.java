package com.rikkei.jobboard.company;
import com.rikkei.jobboard.company.dto.*;
import jakarta.validation.Valid;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/companies")
public class CompanyController {
    private final CompanyService service;
    public CompanyController(CompanyService s){ this.service=s; }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CompanyResponse create(@Valid @RequestBody CompanyRequest req){ return service.create(req); }

    @GetMapping("/{id}")
    public CompanyResponse getById(@PathVariable Long id){ return service.getById(id); }

    @GetMapping
    public Page<CompanyResponse> getAll(@RequestParam(defaultValue="0") int page,
                                        @RequestParam(defaultValue="10") int size,
                                        @RequestParam(required=false) String sort){
        // sort format field,direction - simple delegation via Sort
        Pageable pageable = PageRequest.of(page, size, parseSort(sort));
        return service.getAll(pageable);
    }
    private Sort parseSort(String sort){
        if(sort==null||sort.isBlank()) return Sort.unsorted();
        String[] p=sort.split(",");
        String field=p[0].trim();
        Sort.Direction dir = p.length>1 && p[1].trim().equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        return Sort.by(dir, field);
    }

    @PutMapping("/{id}")
    public CompanyResponse update(@PathVariable Long id, @Valid @RequestBody CompanyRequest req){ return service.update(id, req); }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id){ service.delete(id); }
}
