package com.rikkei.jobboard.job;
import com.rikkei.jobboard.common.exception.*;
import com.rikkei.jobboard.company.CompanyRepository;
import com.rikkei.jobboard.job.dto.JobRequest;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import java.math.BigDecimal;
import java.util.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JobServiceTest {
    @Mock JobRepository jobRepo;
    @Mock CompanyRepository companyRepo;
    @InjectMocks JobService service;

    @Test void create_companyNotFound(){
        when(companyRepo.findById(99L)).thenReturn(Optional.empty());
        var req=new JobRequest(99L,"Java Dev","desc",new BigDecimal("100"),new BigDecimal("200"),"HN",JobStatus.OPEN);
        assertThatThrownBy(()->service.create(req)).isInstanceOf(ResourceNotFoundException.class);
    }
    @Test void getById_notFound(){
        when(jobRepo.findById(99L)).thenReturn(Optional.empty());
        assertThatThrownBy(()->service.getById(99L)).isInstanceOf(ResourceNotFoundException.class);
    }
    @Test void search_returnsPage(){
        when(jobRepo.findAll(any(org.springframework.data.jpa.domain.Specification.class), any(Pageable.class)))
            .thenReturn(new PageImpl<>(List.of()));
        var page=service.search("java","HN",JobStatus.OPEN,PageRequest.of(0,10));
        assertThat(page.getContent()).isEmpty();
        assertThat(page.getTotalElements()).isZero();
    }
    @Test void search_nullParams_ok(){
        when(jobRepo.findAll(any(org.springframework.data.jpa.domain.Specification.class), any(Pageable.class)))
            .thenReturn(new PageImpl<>(List.of()));
        var page=service.search(null,null,null,PageRequest.of(0,10));
        assertThat(page).isNotNull();
    }
    @Test void search_invalidSort_rejected(){
        assertThatThrownBy(()->service.search(null,null,null,PageRequest.of(0,10,Sort.by("password"))))
            .isInstanceOf(BadRequestException.class);
    }
    @Test void delete_notFound(){
        when(jobRepo.findById(99L)).thenReturn(Optional.empty());
        assertThatThrownBy(()->service.delete(99L)).isInstanceOf(ResourceNotFoundException.class);
    }
}
