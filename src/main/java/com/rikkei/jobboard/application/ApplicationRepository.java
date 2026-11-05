package com.rikkei.jobboard.application;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
public interface ApplicationRepository extends JpaRepository<Application, Long> {
    boolean existsByJobIdAndEmail(Long jobId, String email);
    Page<Application> findByJobId(Long jobId, Pageable pageable);
}
