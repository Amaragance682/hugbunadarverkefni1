package com.hugbo.clock_in.dto.request;

import jakarta.validation.constraints.Size;

public class CompanyPatchRequestDTO {
	@Size(min = 2, max = 256, message = "Company name must be between 2 and 256 characters")
	public String name;
}
