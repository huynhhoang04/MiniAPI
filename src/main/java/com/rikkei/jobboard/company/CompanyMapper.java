package com.rikkei.jobboard.company;
import com.rikkei.jobboard.company.dto.*;
public final class CompanyMapper {
    private CompanyMapper(){}
    public static Company toEntity(CompanyRequest r){ return new Company(r.name(), r.address(), r.website()); }
    public static CompanyResponse toResponse(Company c){ return new CompanyResponse(c.getId(), c.getName(), c.getAddress(), c.getWebsite(), c.getCreatedAt(), c.getUpdatedAt()); }
    public static void update(Company c, CompanyRequest r){ c.setName(r.name()); c.setAddress(r.address()); c.setWebsite(r.website()); }
}
