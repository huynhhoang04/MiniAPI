package com.rikkei.jobboard.company;
import com.rikkei.jobboard.common.exception.ResourceNotFoundException;
import com.rikkei.jobboard.company.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CompanyService {
    private final CompanyRepository companyRepository;
    public CompanyService(CompanyRepository r){ this.companyRepository=r; }

    public CompanyResponse create(CompanyRequest req){
        return CompanyMapper.toResponse(companyRepository.save(CompanyMapper.toEntity(req)));
    }
    @Transactional(readOnly=true)
    public CompanyResponse getById(Long id){
        Company c = companyRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Company not found with id: "+id));
        return CompanyMapper.toResponse(c);
    }
    @Transactional(readOnly=true)
    public Page<CompanyResponse> getAll(Pageable pageable){ return companyRepository.findAll(pageable).map(CompanyMapper::toResponse); }

    public CompanyResponse update(Long id, CompanyRequest req){
        Company c = companyRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Company not found with id: "+id));
        CompanyMapper.update(c, req);
        return CompanyMapper.toResponse(companyRepository.save(c));
    }
    public void delete(Long id){
        Company c = companyRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Company not found with id: "+id));
        companyRepository.delete(c);
    }
}
