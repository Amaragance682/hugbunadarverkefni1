package com.hugbo.clock_in.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TaskPatchRequestDTO {
    public String name;
    public String description;
    @Builder.Default
    public Boolean isFinished = false;
}
