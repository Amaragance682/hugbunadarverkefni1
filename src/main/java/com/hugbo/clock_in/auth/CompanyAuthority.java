package com.hugbo.clock_in.auth;

import org.springframework.security.core.GrantedAuthority;

import com.hugbo.clock_in.domain.entity.Company;
import com.hugbo.clock_in.domain.entity.Contract;
import com.hugbo.clock_in.domain.entity.Role;

public class CompanyAuthority implements GrantedAuthority {
    public Company company;
    public Role role;

    public CompanyAuthority(Company company, Role role) {
        this.company = company;
        this.role = role;
    }

    public static CompanyAuthority fromEntity(Contract contract) {
        return new CompanyAuthority(contract.company, contract.role);
    }

    @Override
    public String getAuthority() {
        return String.format("COMPANY_%d_%s", company.name, role);
    }

    @Override
    public String toString() {
        return getAuthority();
    }
}
