package com.hugbo.clock_in.dto.request;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LocationPatchRequestDTO {
    @Size(max = 128)
    public String name;
    @Size(max = 256)
    public String address;
}
