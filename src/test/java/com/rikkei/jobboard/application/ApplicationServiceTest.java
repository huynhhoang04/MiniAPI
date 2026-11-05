package com.rikkei.jobboard.application;
import com.rikkei.jobboard.application.dto.ApplicationRequest;
import com.rikkei.jobboard.common.exception.*;
import com.rikkei.jobboard.job.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ApplicationServiceTest {
    @Mock ApplicationRepository appRepo;
    @Mock JobRepository jobRepo;
    @InjectMocks ApplicationService service;

    private Job openJob(){ Job j=new Job(); j.setId(1L); j.setStatus(JobStatus.OPEN); return j; }

    @Test void apply_ok(){
        when(jobRepo.findById(1L)).thenReturn(Optional.of(openJob()));
        when(appRepo.existsByJobIdAndEmail(1L,"a@x.com")).thenReturn(false);
        when(appRepo.save(any())).thenAnswer(i->i.getArgument(0));
        var res = service.apply(1L, new ApplicationRequest("A","a@x.com","https://x.com/cv"));
        assertThat(res.jobId()).isEqualTo(1L);
        assertThat(res.email()).isEqualTo("a@x.com");
    }
    @Test void apply_jobNotFound(){
        when(jobRepo.findById(99L)).thenReturn(Optional.empty());
        assertThatThrownBy(()->service.apply(99L, new ApplicationRequest("A","a@x.com","https://x.com/cv")))
            .isInstanceOf(ResourceNotFoundException.class);
    }
    @Test void apply_jobClosed(){
        Job j=openJob(); j.setStatus(JobStatus.CLOSED);
        when(jobRepo.findById(1L)).thenReturn(Optional.of(j));
        assertThatThrownBy(()->service.apply(1L, new ApplicationRequest("A","a@x.com","https://x.com/cv")))
            .isInstanceOf(ConflictException.class);
    }
    @Test void apply_duplicateEmail(){
        when(jobRepo.findById(1L)).thenReturn(Optional.of(openJob()));
        when(appRepo.existsByJobIdAndEmail(1L,"a@x.com")).thenReturn(true);
        assertThatThrownBy(()->service.apply(1L, new ApplicationRequest("A","a@x.com","https://x.com/cv")))
            .isInstanceOf(ConflictException.class);
    }
    @Test void getByJobId_jobNotFound(){
        when(jobRepo.findById(99L)).thenReturn(Optional.empty());
        assertThatThrownBy(()->service.getByJobId(99L, org.springframework.data.domain.PageRequest.of(0,10)))
            .isInstanceOf(ResourceNotFoundException.class);
    }
}
