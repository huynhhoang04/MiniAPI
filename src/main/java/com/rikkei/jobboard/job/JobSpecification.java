package com.rikkei.jobboard.job;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import java.util.ArrayList;
import java.util.List;

public final class JobSpecification {
    private JobSpecification(){}

    public static Specification<Job> search(String keyword, String location, JobStatus status){
        return (root, query, cb) -> {
            List<Predicate> preds = new ArrayList<>();
            if (keyword != null && !keyword.isBlank()) {
                String kw = "%" + keyword.toLowerCase() + "%";
                preds.add(cb.or(
                        cb.like(cb.lower(root.get("title")), kw),
                        cb.like(cb.lower(root.get("description")), kw)));
            }
            if (location != null && !location.isBlank()) {
                preds.add(cb.like(cb.lower(root.get("location")), "%" + location.toLowerCase() + "%"));
            }
            if (status != null) {
                preds.add(cb.equal(root.get("status"), status));
            }
            return cb.and(preds.toArray(new Predicate[0]));
        };
    }
}
