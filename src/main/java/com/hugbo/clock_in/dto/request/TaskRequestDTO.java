package com.hugbo.clock_in.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TaskRequestDTO {
    @NotBlank(message = "Task must have a name")
    public String name;

    @Builder.Default
    public String description = "";

    @Builder.Default
    public Boolean isFinished = false;
}
