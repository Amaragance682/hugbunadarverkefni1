package com.hugbo.clock_in.dto.request;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LocationRequestDTO {
    @NotBlank(message = "Location must have a name")
    @Size(max = 128)
    public String name;

    @NotBlank(message = "Location must have an address")
    @Size(max = 256)
    public String address;
}
