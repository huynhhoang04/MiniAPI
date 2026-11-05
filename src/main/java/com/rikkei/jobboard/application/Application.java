package com.rikkei.jobboard.application;
import com.rikkei.jobboard.job.Job;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "application",
       uniqueConstraints = @UniqueConstraint(name="uq_application_job_email", columnNames={"job_id","email"}))
public class Application {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_id", nullable = false)
    private Job job;
    @Column(name="full_name", nullable=false)
    private String fullName;
    @Column(nullable=false)
    private String email;
    @Column(name="cv_url", nullable=false)
    private String cvUrl;
    @Column(name="applied_at", nullable=false)
    private LocalDateTime appliedAt;

    @PrePersist void onCreate(){ if(appliedAt==null) appliedAt=LocalDateTime.now(); }

    public Application(){}
    public Long getId(){return id;} public void setId(Long id){this.id=id;}
    public Job getJob(){return job;} public void setJob(Job j){this.job=j;}
    public String getFullName(){return fullName;} public void setFullName(String n){fullName=n;}
    public String getEmail(){return email;} public void setEmail(String e){email=e;}
    public String getCvUrl(){return cvUrl;} public void setCvUrl(String u){cvUrl=u;}
    public LocalDateTime getAppliedAt(){return appliedAt;} public void setAppliedAt(LocalDateTime t){appliedAt=t;}
}
