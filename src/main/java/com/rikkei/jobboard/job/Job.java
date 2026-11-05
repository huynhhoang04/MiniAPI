package com.rikkei.jobboard.job;
import com.rikkei.jobboard.company.Company;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "job")
public class Job {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;
    @Column(nullable = false)
    private String title;
    @Column(columnDefinition = "TEXT")
    private String description;
    @Column(name = "salary_min")
    private BigDecimal salaryMin;
    @Column(name = "salary_max")
    private BigDecimal salaryMax;
    private String location;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private JobStatus status;
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist void onCreate(){ LocalDateTime n=LocalDateTime.now(); createdAt=n; updatedAt=n; }
    @PreUpdate void onUpdate(){ updatedAt=LocalDateTime.now(); }

    public Job(){}
    public Long getId(){return id;} public void setId(Long id){this.id=id;}
    public Company getCompany(){return company;} public void setCompany(Company c){this.company=c;}
    public String getTitle(){return title;} public void setTitle(String t){this.title=t;}
    public String getDescription(){return description;} public void setDescription(String d){this.description=d;}
    public BigDecimal getSalaryMin(){return salaryMin;} public void setSalaryMin(BigDecimal v){this.salaryMin=v;}
    public BigDecimal getSalaryMax(){return salaryMax;} public void setSalaryMax(BigDecimal v){this.salaryMax=v;}
    public String getLocation(){return location;} public void setLocation(String l){this.location=l;}
    public JobStatus getStatus(){return status;} public void setStatus(JobStatus s){this.status=s;}
    public LocalDateTime getCreatedAt(){return createdAt;} public void setCreatedAt(LocalDateTime t){createdAt=t;}
    public LocalDateTime getUpdatedAt(){return updatedAt;} public void setUpdatedAt(LocalDateTime t){updatedAt=t;}
}
