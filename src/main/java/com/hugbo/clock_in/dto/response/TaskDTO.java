package com.hugbo.clock_in.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TaskDTO {
    public Long id;
    public CompanyDTO company;
    public LocationDTO location;
    public String name;
    public String description;
    @Builder.Default
    public Boolean isFinished = false;
}
