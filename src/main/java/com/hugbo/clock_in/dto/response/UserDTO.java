package com.hugbo.clock_in.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDTO {
    public Long id;
    public String name;
    public String email;

    @Builder.Default
    public Boolean admin = false;
}
