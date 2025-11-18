package com.hugbo.clock_in.controller.companies;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hugbo.clock_in.auth.CustomUserDetails;
import com.hugbo.clock_in.dto.request.CompanyPatchRequestDTO;
import com.hugbo.clock_in.dto.request.CompanyRequestDTO;
import com.hugbo.clock_in.dto.request.ContractRequestDTO;
import com.hugbo.clock_in.dto.response.CompanyDTO;
import com.hugbo.clock_in.dto.response.ContractDTO;
import com.hugbo.clock_in.service.CompanyService;
import com.hugbo.clock_in.service.ContractService;

import jakarta.validation.Valid;

@RequestMapping("/companies")
@RestController
public class CompanyController {
    @Autowired
    private CompanyService companyService;
    @Autowired
    private ContractService contractService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getCompanies() {
        List<CompanyDTO> companyDTOs = companyService.getAllCompanies();
        return ResponseEntity.ok()
            .body(companyDTOs);
    }

    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getMyCompanies(
        @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        List<CompanyDTO> companyDTOs = companyService.getContractedCompanies(customUserDetails.getId());
        return ResponseEntity.ok()
            .body(companyDTOs);
    }

    @GetMapping("/{companyId}")
    @PreAuthorize("@securityService.isCompanyManager(authentication.principal.id, #companyId) or hasRole('ADMIN')")
    public ResponseEntity<?> getCompany(@PathVariable Long companyId) {
        CompanyDTO companyDTO = companyService.getCompany(companyId);
        return ResponseEntity.ok()
            .body(companyDTO);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> addCompany(@Valid @RequestBody CompanyRequestDTO companyRequestDTO) {
        try {
            CompanyDTO companyDTO = companyService.addCompany(companyRequestDTO);
            return ResponseEntity.ok()
                .body(companyDTO);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(e.getMessage());
        }
    }

    @PatchMapping("/{companyId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> patchCompany(
            @Valid @RequestBody CompanyPatchRequestDTO companyPatchRequestDTO,
            @PathVariable Long companyId
            ) {
        try {
            CompanyDTO companyDTO = companyService.patchCompany(companyId, companyPatchRequestDTO);
            return ResponseEntity.ok()
                .body(companyDTO);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(e.getMessage());
        }
    }

    @PostMapping("/{companyId}/add/{userId}")
    @PreAuthorize("@securityService.isCompanyManager(authentication.principal.id, #companyId) or hasRole('ADMIN')")
    public ResponseEntity<?> signToCompany(
            @PathVariable Long companyId,
            @PathVariable Long userId,
            @Valid @RequestBody ContractRequestDTO contractRequestDTO
            ) {
        ContractDTO contractDTO = contractService.signToCompany(userId, companyId, contractRequestDTO);

        return ResponseEntity.ok().body(contractDTO);
    }

    @DeleteMapping("/{companyId}/resign")
    public ResponseEntity<?> resignFromCompany(
        @PathVariable Long companyId,
        @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        contractService.resign(customUserDetails.getId(), companyId);
        return ResponseEntity.ok().body("You have resigned from the company");
    }

    @DeleteMapping("/{companyId}/resign/{userId}")
    @PreAuthorize("@securityService.isCompanyManager(authentication.principal.id, #companyId) or hasRole('ADMIN')")
    public ResponseEntity<?> fireUser(
        @PathVariable Long companyId,
        @PathVariable Long userId
    ) {
        contractService.resign(userId, companyId);
        return ResponseEntity.ok().body("The user has been fired from the company");
    }
}
